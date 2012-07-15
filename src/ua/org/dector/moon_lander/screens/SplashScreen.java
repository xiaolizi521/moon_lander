package ua.org.dector.moon_lander.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import ua.org.dector.gcore.common.Settings;
import ua.org.dector.gcore.game.AbstractScreen;
import ua.org.dector.moon_lander.graphics.Graphics;
import ua.org.dector.moon_lander.LanderGame;

import static ua.org.dector.moon_lander.AppConfig.*;

/**
 * @author dector (dector9@gmail.com)
 */
public class SplashScreen extends AbstractScreen {
    private LanderGame landerGame;

    private TextureRegion gaminatorLogo;
    private TextureRegion dedication;

    private Sound fadeInSound;

    private Stage stage;

    private boolean completed;

    public SplashScreen(LanderGame landerGame) {
        super(landerGame);

        this.landerGame = landerGame;

        fadeInSound = landerGame.getResourceLoader().loadSound(FADEIN_FILE);

        Texture splashTex = landerGame.getResourceLoader().loadTexture(SPLASH_FILE);
        gaminatorLogo = new TextureRegion(
                splashTex,
                GAMINATOR_LOGO_WIDTH,
                GAMINATOR_LOGO_HEIGHT
        );
        dedication = new TextureRegion(
                splashTex,
                GAMINATOR_LOGO_WIDTH,
                0,
                DEDICATION_WIDTH,
                DEDICATION_HEIGHT
        );

        Settings gameSettings = landerGame.getSettings();
        int screenWidth = gameSettings.getScreenWidth();
        int screenHeight = gameSettings.getScreenHeight();

        stage = new Stage(screenWidth, screenHeight, false);

        Action gaminatorLogoAct = Sequence.$(
                MoveTo.$((screenWidth / 2 - GAMINATOR_LOGO_WIDTH) / 2,
                        (screenHeight - GAMINATOR_LOGO_HEIGHT) / 2,
                        0),
                FadeIn.$(.5f),
                Delay.$(1f)//,
//                FadeOut.$(.5f)
        );
        final Image gaminatorLogoImage = new Image(gaminatorLogo);
        gaminatorLogoImage.color.a = 0;

        final Image dedicationImage = new Image(dedication);
        dedicationImage.color.a = 0;
        Action dedicationAction = Sequence.$(
                MoveTo.$((3 * screenWidth / 2 - DEDICATION_WIDTH) / 2,
                        (screenHeight - DEDICATION_HEIGHT) / 2,
                        0),
                FadeIn.$(.5f),
                Delay.$(1f)//,
//                FadeOut.$(.5f)
        );
        dedicationAction.setCompletionListener(
                new OnActionCompleted() {
                    public void completed(Action action) {
                        SplashScreen.this.completed = true;
                    }
                });
        dedicationImage.action(dedicationAction);

        gaminatorLogoAct.setCompletionListener(new OnActionCompleted() {
            public void completed(Action action) {
                stage.addActor(dedicationImage);
                fadeInSound.play();
            }
        });
        gaminatorLogoImage.action(gaminatorLogoAct);

        // Very dirty hack

        Label emptyActor = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Action startedAct = Sequence.$(Delay.$(0.05f), Remove.$());
        startedAct.setCompletionListener(new OnActionCompleted() {
            public void completed(Action action) {
                stage.addActor(gaminatorLogoImage);
                fadeInSound.play();
            }
        });
        emptyActor.action(startedAct);
        stage.addActor(emptyActor);
    }

    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        } else if (keycode == Input.Keys.SPACE) {
            landerGame.play();
        }

        return true;
    }

    public void render(float delta) {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();


        int screenWidth = ((LanderGame)game).getSettings().getScreenWidth();

        if (completed) {
            Graphics g = game.getGraphics();
            g.begin();
            g.drawCentered("Press <<Space>> to start",
                    screenWidth / 2, 100, Graphics.FontSize.BIG);
            g.end();
        }
    }

    public void dispose() {
        gaminatorLogo.getTexture().dispose();
        stage.dispose();
    }
}