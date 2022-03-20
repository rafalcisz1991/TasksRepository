package com.crud.tasks.controller;

import com.crud.tasks.domain.CreatedTrelloCard;
import com.crud.tasks.domain.NewTrelloCardDto;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import com.crud.tasks.trello.client.TrelloClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/trello")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TrelloController {

    private final TrelloClient trelloClient;

    @GetMapping("boards")
    public List<TrelloBoardDto> getTrelloBoards() {

        List<TrelloBoardDto> trelloBoards = trelloClient.getTrelloBoards();

        List<TrelloBoardDto> filteredTrelloBoards =
                trelloBoards.stream()
                        .filter(l -> !l.getName().isEmpty())
                        .filter(l -> !l.getId().isEmpty())
                        .filter(l -> l.getName().contains("Kodilla"))
                        .collect(Collectors.toList());

        filteredTrelloBoards.forEach(trelloBoardDto -> {
            System.out.println("This board contains lists: ");
            System.out.println(trelloBoardDto.getId() + " " + trelloBoardDto.getName());
            trelloBoardDto.getLists().forEach(trelloList -> {
                System.out.println(trelloList.getName() + " - " + trelloList.getId() + " - " + trelloList.isClosed());
            });
        });
        return filteredTrelloBoards;
    }

    @GetMapping("batches")
    public List<NewTrelloCardDto> getTrelloBatches() {

        List<NewTrelloCardDto> trelloCards = trelloClient.getTrelloCardsWithMoreFields();

        System.out.println("This board contains following cards (IDs), with underlying batches");
        trelloCards.forEach(trelloCardDto -> {
            System.out.println(trelloCardDto.getCardId());
            System.out.println();
        });
        return trelloCards;
    }

    @PostMapping("cards")
    public CreatedTrelloCard createTrelloCard(@RequestBody TrelloCardDto trelloCardDto) {
        return trelloClient.createNewCard(trelloCardDto);
    }
}
