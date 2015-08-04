package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FillViewport;



/**
 * Created by eli on 03/08/2015.
 */
public class TelaJogo extends TelaBase {

    private OrthographicCamera camera;
    private SpriteBatch  batch;
    private Stage palco;
    private BitmapFont fonte;
    private Label lbPontuacao;

    /**
     * Contrutor padr�o da tela de jogo

     * @param game Refer�ncia para classe principal
     */
    public TelaJogo (MainGame game){
        super(game);

    }

    /**
     * chamada quando a tela � exibida
     */

    @Override
    public void show() {
      camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight( ));
      batch = new SpriteBatch();
      palco = new Stage(new FillViewport(camera.viewportWidth, camera.viewportHeight, camera));

        initFonte();
        initInformacoes();
    }

    private void initInformacoes(){
        Label.LabelStyle lbEstilo  = new  Label.LabelStyle();
        lbEstilo.font = fonte;
        lbEstilo.fontColor = Color.WHITE;

        lbPontuacao = new Label("0 pontos", lbEstilo);
        palco.addActor(lbPontuacao);


    }

    private  void initFonte(){
      fonte = new BitmapFont();
    }



    /**
     * Chamado a todo quadro de atualiza��o de Jogo (FPS)
     * @param delta Tempo entre um quadro e outro (em segundo)
     */

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.15f,.15f,.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        lbPontuacao.setPosition(10, camera.viewportHeight - 20);

        palco.act(delta);
        palco.draw();


    }

    /**
     * � chamado sempre h� uma altera��o no tamanho da tela
     * @param width Novo valor de largura da tela
     * @param height Novo valor de altura da tela
     */

    @Override
    public void resize(int width, int height) {
     camera.setToOrtho(false, width, height);
     camera.update();
    }

    /**
     * � chamado sempre que o jogo for minimizado
     */
    @Override
    public void pause() {

    }

    /**
     * � chamado sempre quando o jogo voltar para o primeiro plano
     */
    @Override
    public void resume() {

    }

    /**
     * � chamado quando a tela for destruida
     */
    @Override
    public void dispose() {

        batch.dispose();
        palco.dispose();
        fonte.dispose();

    }
}
