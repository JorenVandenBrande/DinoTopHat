package com.dinoth.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.dinoth.game.DinoTopHat;
import com.dinoth.logic.PcControls;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(960, 540);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new DinoTopHat(new PcControls());
        }
}