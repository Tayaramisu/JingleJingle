package com.tayaramisu;

public enum Sound {
    AGILITY_PYRAMID("Agility_Pyramid_Pyramid_Top.wav"),
    AIR_GUITAR("Air_Guitar.wav"),
    BARB_ASS_WAVE("Barb_Ass_Wave_Complete.wav"),
    BARROWS_COMPLETE("Barrows_Complete.wav"),
    BURTHORPE_GAMES("Burthorpe_Games_Room_Draw.wav"),
    CACTI_CHECK("Cacti_Health_Check.wav"),
    CASTLE_WARS_WIN("Castle_Wars_Win.wav"),
    DELIVERY_GNOME("Delivery_Gnome_Restaurant.wav"),
    DICE_WIN("Dice_Win.wav"),
    DORGESHUUN_SUNSHINE("Dorgeshuun_First_Sunshine.wav"),
    DUEL_ARENA_DUEL("Duel_Arena_Start_of_Duel.wav"),
    EASTER_2005("Easter_2005_Scape_Scrambled.wav"),
    FARMING_AMULET("Farming_Amulet.wav"),
    FIGHT_CAVE_WAVE("Fight_Caves_Wave_Complete.wav"),
    FIGHT_PITS_CHAMP("Fight_Pits_Champion.wav"),
    FORGETTABLE_PUZZLE("Forgettable_Puzzle_Completed.wav"),
    FREM_BALLAD_COMPLETE("Fremennik_Ballad_Completed.wav"),
    FREM_BALLAD_OPENING("Fremennik_Ballad_Opening.wav"),
    FREM_BALLAD_REFRAIN("Fremennik_Ballad_Refrain.wav"),
    FREM_BERATING_KING("Fremennik_Berating_the_King.wav"),
    GIANTS_FOUNDRY_HAND("Giants_Foundry_Handing_In.wav"),
    GNOMEBALL_GOAL("Gnomeball_Goal.wav"),
    GNOME_SUCCESS("Gnome_Success_Speedy.wav"),
    GOTR_RIFT_CLOSES("GOTR_Rift_Closes.wav"),
    JORMUNGAND_DEFEATED("Jormungand_Defeated.wav"),
    KELDAGRIM_TRADING("Keldagrim_Trading_Victory.wav"),
    KINGS_RANSOM_VERDICT("King's_Ransom_Trial_Verdict.wav"),
    LEAGUE_AREA_UNLOCK("League_Area_Unlock_Jingle.wav"),
    LEAGUE_RELIC_UNLOCK("League_Relic_Unlock_Jingle.wav"),
    LEAGUE_TASK_COMPLETE("League_Task_Completion_Jingle.wav"),
    LEAGUE_TUT_COMPLETE("League_Tutorial_Complete.wav"),
    MAZE_RANDOM_COMPLETE("Maze_Random_Complete.wav"),
    MM1_JUNGLE_DEMON("MM1_Jungle_Demon_Defeated.wav"),
    MOURN_END_II_CRYSTAL("Mourning's_End_II_Crystal.wav"),
    PEST_CONTROL_WIN("Pest_Control_Win.wav"),
    PICKPOCKET_FAIRY_GOD("Pickpocketing_Fairy_Godfather.wav"),
    POSTIE_PETE_THEME("Postie_Pete_theme.wav"),
    PRISON_PETE_SUCCESS("Prison_Pete_Random_Success.wav"),
    RATCATCHER_KING_DIES("Ratcatchers_King_Rat_Dies.wav"),
    REACTIVATE_WATCHTOWR("Reactivating_the_Watchtower.wav"),
    RECRUIT_DRIVE_1("Recruit_Drive_Hynn_Terprett.wav"),
    RECRUIT_DRIVE_2("Recruit_Drive_Kuam_Ferentse.wav"),
    RECRUIT_DRIVE_3("Recruit_Drive_Lady_Table.wav"),
    RECRUIT_DRIVE_4("Recruit_Drive_Sir_Spishyus.wav"),
    RFD_LUMBRIDGE_GUIDE("RFD_Lumbridge_Guide_Quiz.wav"),
    SCHEMATICS_COMPLETED("Schematics_Completed.wav"),
    SECURITY_BOX_HEALTH("Security_Box_of_Health.wav"),
    SHAIKAHAN_DEFEATED("Shaikahan_Defeated.wav"),
    SHOOT_STAR_DISCOVER("Shooting_Stars_Discoverer.wav"),
    SKULLBALL_GOAL("Werewolf_Skullball_Goal.wav"),
    SOUL_WARS_WIN("Soul_Wars_Win.wav"),
    SWAMP_BOATY("Swamp_Boaty.wav"),
    TEMPLE_TREK_SUCCESS("Temple_Trekking_Event_Success.wav"),
    TEMPLE_TREK_COMPLETE("Temple_Trekking_Trek_Complete.wav"),
    TOB_WAVE_COMPLETE("Theatre_of_Blood_Wave_Complete.wav"),
    TIADECHE_RETURNS("Tiadeche_Returns.wav"),
    TINSAY_RETURNS("Tinsay_Returns.wav"),
    TOA_PATH_COMPLETE("TOA_Path_Challenge_Complete.wav"),
    WILY_CAT_THEME("Wily_Cat_Theme.wav");

    private final String resourceName;

    Sound(String resNam) {
        this(resNam, false);
    }

    Sound(String resNam, boolean streamTroll) {
        resourceName = resNam;
    }

    String getResourceName() {
        return resourceName;
    }
}