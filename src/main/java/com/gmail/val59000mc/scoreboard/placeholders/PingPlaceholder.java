package com.gmail.val59000mc.scoreboard.placeholders;

import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.scoreboard.Placeholder;
import com.gmail.val59000mc.scoreboard.ScoreboardType;

import org.bukkit.entity.Player;

public class PingPlaceholder extends Placeholder {

    public PingPlaceholder() {
        super("ping");
    }

    @Override
    public String getReplacement(UhcPlayer uhcPlayer, Player player, ScoreboardType scoreboardType,
            String placeholder) {
        try {
            return String.valueOf(uhcPlayer.getPlayer().getPing());
        } catch (UhcPlayerNotOnlineException e) {
            return "";
        }
    }


    
}
