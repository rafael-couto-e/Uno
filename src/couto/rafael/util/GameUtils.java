package couto.rafael.util;

import couto.rafael.model.*;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public final class GameUtils {
    private static Order currentOrder;
    private static int currentPlayer;
    private static List<Player> players;
    private static List<Card> deck;
    private static Table table;
    private static Scanner s;
    private static int cumulation;

    public static void init(List<Player> players, Table table, List<Card> deck){
        GameUtils.players = players;
        GameUtils.table = table;
        GameUtils.deck = deck;

        currentOrder = Order.CLOCKWISE;
        currentPlayer = 0;
        cumulation = 1;

        s = new Scanner(System.in);

        if(currentPlayerIsCpu())
            performCpuAction();
        else
            requestPlayerAction();
    }

    private static void requestPlayerAction(){
        displayTableTop();
        requestCardTo(getCurrentPlayer(), getNextPlayer());
        finishPlayerAction();
    }

    private static void displayTableTop() {
        System.out.println("Carta na mesa: "+table.getTableTop());
    }

    private static int next(int max) {
        int next;

        if(currentOrder == Order.CLOCKWISE)
            next = currentPlayer < max-1 ? currentPlayer+1 : 0;
        else
            next = currentPlayer > 0 ? currentPlayer-1 : max-1;

        return next;
    }

    private static void finishPlayerAction(){
        finishAction();
        if(getCurrentPlayer().getName().contains("CPU"))
            performCpuAction();
        else
            requestPlayerAction();
    }

    private static void finishAction() {
        currentPlayer = next(players.size());
        System.out.println("Rodada finalizada.");
    }

    private static void performCpuAction(){
        Player cpu = getCurrentPlayer();
        System.out.println("JOGADOR: "+cpu.getName());
        Card tableTop = table.getTableTop();
        Card playedCard = null;

        for(int i = 0; i < cpu.getHand().size(); i++){
            Card c = cpu.getHand().get(i);
            if(c.getColor() == tableTop.getColor() ||
                    c.getNumber() == tableTop.getNumber() ||
                    c.getColor() == Color.BLACK){
                playedCard = c;
                cpu.getHand().remove(i);
                break;
            }
        }

        if(playedCard != null){
            TableUtils.put(table, playedCard);
            if(playedCard instanceof SpecialCard)
                performSpecialAction(getNextPlayer(), (SpecialCard) playedCard);
        }else{
            DeckUtils.buy(cpu, deck);
        }

        checkVictory(getCurrentPlayer());

        finishPlayerAction();
    }

    private static void performSpecialAction(Player target, SpecialCard card){
        switch (card.getEffect()){
            case PLUS2: case PLUS4:
                if(targetIsVulnerable(target, card)) {
                    accept(target, card.getEffect());

                    if(card.getEffect() == Effect.PLUS4)
                        if(getCurrentPlayer().getName().contains("CPU"))
                            card.setColor(requestColorForCpu());
                        else
                            card.setColor(requestColor());
                } else {
                    if(getCurrentPlayer().getName().contains("CPU"))
                        requestResponseForCpu(target, card.getEffect());
                    else
                        requestResponse(target, card);
                }
                break;
            case SKIP:
                currentPlayer = next(players.size());
                break;
            case COLOR:
                if(getCurrentPlayer().getName().contains("CPU"))
                    card.setColor(requestColorForCpu());
                else
                    card.setColor(requestColor());
                break;
            case REVERSE:
                revertOrder();
                break;
        }

        checkVictory(getCurrentPlayer());
    }

    private static void requestResponse(Player target, SpecialCard sCard) {
        Card c = null;

        System.out.println("Escolha uma ação: ");
        System.out.println("1 - Jogar uma carta");
        System.out.println("2 - Aceitar seu destino");

        int op;

        do{
            op = s.nextInt();

            switch (op){
                case 1:
                    int card;
                    boolean valid = false;

                    while(!valid){
                        System.out.println("Escolha uma entre as cartas possíveis: ");
                        displayCardsFor(target, sCard.getEffect());

                        card = s.nextInt();

                        if(target.getHand().get(card) instanceof SpecialCard){
                            SpecialCard mScard = (SpecialCard) target.getHand().get(card);

                            if(mScard.getEffect() == sCard.getEffect()){
                                c = mScard;
                                valid = true;
                                target.getHand().remove(card);
                                TableUtils.put(table, c);
                                checkVictory(getCurrentPlayer());
                            }else{
                                System.out.println("Essa carta não pode ser jogada!");
                            }
                        }else{
                            System.out.println("Essa carta não pode ser jogada!");
                        }
                    }
                    break;
                case 2:
                    System.out.println("Destino aceito.");
                    if(sCard.getEffect() == Effect.PLUS4)
                        if(getCurrentPlayer().getName().contains("CPU"))
                            sCard.setColor(requestColorForCpu());
                        else
                            sCard.setColor(requestColor());
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }while (op < 1 || op > 2);

        if(c != null) respond(c);
        else accept(target, sCard.getEffect());
    }

    private static void displayCardsFor(Player target, Effect effect) {
        for(int i = 0; i < target.getHand().size(); i++){
            Card c = target.getHand().get(i);

            if(c instanceof SpecialCard){
                if (((SpecialCard) c).getEffect() == effect){
                    System.out.println(i+" - "+c.toString());
                }
            }
        }
    }

    private static void checkVictory(Player current){
        if(current.getHand().size() == 0) declareVictory(current);
    }

    private static void displayCardsFor(Player player) {
        for(int i = 0; i < player.getHand().size(); i++){
            Card c = player.getHand().get(i);
            System.out.println(i+" - "+c.toString());
        }
    }

    private static void accept(Player target, Effect effect) {
        EffectUtils.performEffectOn(target, deck, effect, cumulation);
        cumulation = 1;
        currentPlayer = next(players.size());
    }

    private static void requestResponseForCpu(Player target, Effect effect) {
        Card played = null;

        for(Card c:target.getHand()){
            if(c instanceof SpecialCard){
                if (((SpecialCard) c).getEffect() == effect){
                    played = c;
                    respond(c);
                    break;
                }
            }
        }

        if(played == null) accept(target, effect);

        checkVictory(getCurrentPlayer());
    }

    private static void respond(Card c) {
        cumulation++;
        finishAction();
        performSpecialAction(getNextPlayer(), (SpecialCard) c);
    }

    private static boolean targetIsVulnerable(Player target, SpecialCard card) {
        for(Card c:target.getHand()){
            if(c instanceof SpecialCard){
                if(((SpecialCard) c).getEffect() == card.getEffect()) return false;
            }
        }

        return true;
    }

    private static Color requestColor() {
        int op;

        do {
            System.out.println("Escolha a cor desejada:");
            System.out.println("1 - Azul");
            System.out.println("2 - Verde");
            System.out.println("3 - Vermelho");
            System.out.println("4 - Amarelo");
            op = s.nextInt();

            switch (--op){
                case 0:case 1:case 2:case 3:
                    return ColorUtils.getColor(op);
                default:
                    System.out.println("Cor inválida!");
                    break;
            }
        }while (op < 0 || op > 4);

        return Color.BLACK;
    }

    private static Color requestColorForCpu() {
        int random = (int) new Random().nextDouble()*5;

        switch (random){
            case 0:case 1:case 2:
                return ColorUtils.getColor(random);
        }

        return ColorUtils.getColor(3);
    }

    private static void revertOrder() {
        if(currentOrder == Order.CLOCKWISE)
            currentOrder = Order.ANTICLOCKWISE;
        else
            currentOrder = Order.CLOCKWISE;
    }

    private static void declareVictory(Player player){
        System.out.println("O jogador "+player.getName()+" venceu!");
        System.exit(0);
    }

    private static void requestCardTo(Player current, Player next){
        System.out.println("JOGADOR: "+current.getName());
        Card playedCard = null;

        displayCardsFor(current);
        System.out.println(current.getHand().size()+" - comprar");

        boolean valid;

        do{
            valid = false;

            System.out.println("Informe a carta a ser jogada: ");
            int card = s.nextInt();

            if(card >= 0 && card < current.getHand().size()){
                Card c = current.getHand().get(card);

                if(canPlay(c)){
                    valid = true;
                    playedCard = c;
                    current.getHand().remove(card);
                }else{
                    System.out.println("Não é possível jogar essa carta!");
                }
            }else if(card == current.getHand().size()) {
                valid = true;
                playedCard = null;
            }else{
                System.out.println("Carta não encontrada.");
            }
        }while (!valid);

        if(playedCard == null)
            DeckUtils.buy(current, deck);
        else {
            TableUtils.put(table, playedCard);

            if (playedCard instanceof SpecialCard)
                performSpecialAction(next, (SpecialCard) playedCard);

            if (current.getHand().size() == 0) declareVictory(current);

            finishPlayerAction();
        }
    }

    private static boolean canPlay(Card c) {
        Card tableTop = table.getTableTop();
        return tableTop.getNumber() == c.getNumber()
                || tableTop.getColor() == c.getColor()
                ||  c.getColor() == Color.BLACK;
    }

    private static boolean currentPlayerIsCpu(){
        return players.get(currentPlayer).getName().contains("CPU");
    }

    private static Player getCurrentPlayer(){
        return players.get(currentPlayer);
    }

    private static Player getNextPlayer(){
        return players.get(next(players.size()));
    }
}
