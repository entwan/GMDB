package com.dam.gmdb.commons;

public interface NodesNames {

    //CLés du fichier shared preference
    String UPLOAD_PREFS = "dataInsertIntoFireBase";

    // Table films
    String TABLE_FILM = "films";

    // Les clés pour l'association des colonnes dans la base de données **/
    String KEY_TITRE = "titre";
    String KEY_TITRE_MINUSCULE = "titre_minuscule";
    String KEY_ANNEE = "annee";
    String KEY_ACTEURS = "acteurs";
    String KEY_AFFICHE = "affiche";
    String KEY_SYNOPSIS = "synopsis";

    // Les varibles lièes aux emplacements de stockage de Firebase
    String IMAGE_FOLDER = "affiches";
}
