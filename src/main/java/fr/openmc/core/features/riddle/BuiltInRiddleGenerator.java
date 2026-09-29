package fr.openmc.core.features.riddle;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class BuiltInRiddleGenerator implements RiddleGenerator {
    private static final List<Riddle> RIDDLES = List.of(
            new Riddle("Je suis plein de trous, mais je peux retenir de l'eau. Qui suis-je ?", List.of("éponge", "une éponge")),
            new Riddle("Plus je sèche, plus je deviens mouillé. Qui suis-je ?", List.of("serviette", "une serviette")),
            new Riddle("Je peux faire le tour du monde en restant dans un coin. Qui suis-je ?", List.of("timbre", "un timbre")),
            new Riddle("Je monte et je descends sans jamais bouger. Qui suis-je ?", List.of("escalier", "un escalier")),
            new Riddle("J'ai des aiguilles mais je ne sais pas coudre. Qui suis-je ?", List.of("horloge", "une horloge")),
            new Riddle("Je peux avoir des clés mais aucune serrure. Qui suis-je ?", List.of("piano", "un piano")),
            new Riddle("J'ai des dents mais je ne mange jamais. Qui suis-je ?", List.of("peigne", "un peigne")),
            new Riddle("Je cours sans avoir de jambes et je murmure sans avoir de bouche. Qui suis-je ?", List.of("rivière", "une rivière")),
            new Riddle("Je suis toujours devant toi mais tu ne peux jamais me voir. Qui suis-je ?", List.of("futur", "le futur")),
            new Riddle("Plus on m'enlève, plus je deviens grand. Qui suis-je ?", List.of("trou", "un trou")),
            new Riddle("Je peux être cassé sans être touché. Qui suis-je ?", List.of("promesse", "une promesse")),
            new Riddle("Je disparais dès que tu prononces mon nom. Qui suis-je ?", List.of("silence", "le silence")),
            new Riddle("Je n'ai qu'une jambe et un chapeau. Qui suis-je ?", List.of("champignon", "un champignon")),
            new Riddle("Je peux remplir une pièce sans prendre de place. Qui suis-je ?", List.of("lumière", "la lumière")),
            new Riddle("J'ai quatre pattes le matin, deux à midi et trois le soir. Qui suis-je ?", List.of("homme", "un homme")),
            new Riddle("Je suis invisible, mais tu peux me sentir sur ton visage. Qui suis-je ?", List.of("vent", "le vent")),
            new Riddle("Je possède des villes sans maisons et des rivières sans eau. Qui suis-je ?", List.of("carte", "une carte")),
            new Riddle("Je grandis quand je mange et je meurs quand je bois. Qui suis-je ?", List.of("feu", "le feu")),
            new Riddle("J'ai un cou mais pas de tête. Qui suis-je ?", List.of("bouteille", "une bouteille")),
            new Riddle("Je peux être attrapé mais pas lancé. Qui suis-je ?", List.of("rhume", "un rhume")),
            new Riddle("Quel mois a 28 jours ?", List.of("tous", "tous les mois")),
            new Riddle("Je suis une couleur et un fruit. Qui suis-je ?", List.of("orange", "l'orange")),
            new Riddle("Je peux avoir une tête et une queue mais pas de corps. Qui suis-je ?", List.of("pièce", "une pièce", "monnaie")),
            new Riddle("Je suis au milieu de Paris. Qui suis-je ?", List.of("r", "la lettre r")),
            new Riddle("Je peux faire pleurer sans avoir d'yeux. Qui suis-je ?", List.of("oignon", "un oignon")),
            new Riddle("Je peux être lu de gauche à droite ou de droite à gauche et rester le même. Qui suis-je ?", List.of("palindrome", "un palindrome")),
            new Riddle("Je suis invisible, mais je peux remplir tes poumons. Qui suis-je ?", List.of("air", "l'air")),
            new Riddle("Je peux avoir une serrure mais je ne suis pas une porte. Qui suis-je ?", List.of("coffre", "un coffre")),
            new Riddle("Je suis blanc, mais je deviens noir quand je suis sale. Qui suis-je ?", List.of("tableau", "un tableau")),
            new Riddle("Je suis ronde, je roule et je peux rebondir. Qui suis-je ?", List.of("balle", "une balle"))
    );

    @Override
    public CompletableFuture<Riddle> generateRiddle(LocalDate date) {
        int index = Math.floorMod(date.toEpochDay(), RIDDLES.size());
        return CompletableFuture.completedFuture(RIDDLES.get(index));
    }
}
