package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;

import java.util.ArrayList;


/**
 * Created by eli on 03/08/2015.
 */
public class TelaJogo extends TelaBase {

    // objeto do jogo - Camera ortografica 2D;
    private OrthographicCamera camera;
    private SpriteBatch  batch;
    private Stage palco;
    private BitmapFont fonte;
    private Label lbPontuacao;
    private Label lbGameOver;
    private Image jogador;
    private Texture texturaJogador;
    private Texture texturaJogadorDireta;
    private Texture texturaJogadorEsquerda;
    private boolean indoDireta;
    private boolean indoEsquerda;
    private boolean atirando;
    private Array<Image> tiros = new Array<Image>();
    private Texture texturaTiro;
    private Texture texturaMeteoro1;
    private Texture texturaMeteoro2;
    private Array<Image> meteoros1 = new Array<Image>();
    private Array<Image> meteoros2 = new Array<Image>();





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

        initTexturas();
        initFonte();
        initInformacoes();
        initJogador();

    }

    private void initTexturas() {
        texturaTiro = new Texture("sprites/shot.png");
        texturaMeteoro1 = new Texture("sprites/enemie-1.png");
        texturaMeteoro2 = new Texture("sprites/enemie-2.png");


    }

    /**
     * Instacia os objetos do jogador o adiciona ao palco;
     */
    private void initJogador(){
        texturaJogador = new Texture("sprites/player.png");
        texturaJogadorDireta = new Texture("sprites/player-right.png");
        texturaJogadorEsquerda = new Texture("sprites/player-left.png");

        jogador = new Image(texturaJogador);

        float x = camera.viewportWidth / 2 - jogador.getWidth()/ 2;
        float y = 15;

        jogador.setPosition(x, y);

        palco.addActor(jogador);

    }

    /**
     *
     */
    private void initInformacoes(){
        Label.LabelStyle lbEstilo  = new  Label.LabelStyle();
        lbEstilo.font = fonte;
        lbEstilo.fontColor = Color.WHITE;

        lbPontuacao = new Label("0 pontos", lbEstilo);
        palco.addActor(lbPontuacao);

        lbGameOver = new Label("Game Over!", lbEstilo);
        palco.addActor(lbGameOver);



    }

    /**
     * Instacia os objetos de fontes
     */
    private  void initFonte(){
      fonte = new BitmapFont();
    }



    /**
     * Chamado a todo quadro de atualiza��o de Jogo (FPS)
     * @param delta Tempo entre um quadro e outro (em segundo)
     */

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.15f, .15f,.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        lbPontuacao.setPosition(10, camera.viewportHeight - 20);
        lbPontuacao.setText(pontuacao + " Pontos");


        lbGameOver.setPosition(camera.viewportWidth / 2 - lbGameOver.getHeight() / 2, camera.viewportHeight / 2);
        lbGameOver.setVisible(gameOver == true);

        if (gameOver == false){
            captureTeclas();
            atualizarJogador(delta);
            atualizarTiro(delta);
            atualizarMeteoros(delta);
            detectarcolisoes(meteoros1, 5);
            detectarcolisoes(meteoros2, 15);
        }

        // Atualiza a situação do palco
        palco.act(delta);
        // Desenha o palco na tela
        palco.draw();




    }
    private Rectangle recJogador = new Rectangle();
    private Rectangle recTiro = new Rectangle();
    private Rectangle recMeteoro = new Rectangle();

    private int pontuacao = 0;
    private  boolean gameOver = false;

    private void detectarcolisoes(Array<Image> meteoros, int valePontos) {
        recJogador.set(jogador.getX(), jogador.getY(),jogador.getWidth(), jogador.getHeight());

        for (Image meteoro : meteoros){
            recMeteoro.set(meteoro.getX(), meteoro.getY(),meteoro.getWidth(), meteoro.getHeight());
           for (Image tiro : tiros){
            recTiro.set(tiro.getX(), tiro.getY(), tiro.getWidth(), tiro.getHeight());
            if (recMeteoro.overlaps(recTiro)){
                //aqui ocorre uma colisão do tiro com o meteoro 1
                pontuacao += valePontos;
                tiro.remove();//remove do palco
                tiros.removeValue( tiro, true);//remove da lista
                meteoro.remove();//remove do palco
                meteoros.removeValue(meteoro , true); //remove da lista


            }

          }
            //detectar colisões com o player
            if (recJogador.overlaps(recMeteoro)){
             //colisão com o jogador;
                gameOver = true;
            }
        }

    }





    private void atualizarMeteoros(float delta) {
        int qtdMeteoro = meteoros1.size  + meteoros2.size; //retorna a quantidade de meteoro criados


        if (qtdMeteoro < 10 ) {


            int tipo = MathUtils.random(1, 4); //retorna 1 ou 2 aleatoriamente

            if (tipo == 1) {
                //cria meteoro 1
                Image meteoro = new Image(texturaMeteoro1);
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);
                meteoro.setPosition(x, y);
                meteoros1.add(meteoro);
                palco.addActor(meteoro);


            } else {

                //cria meteoro 2
                Image meteoro = new Image(texturaMeteoro2);
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);
                meteoro.setPosition(x, y);
                meteoros2.add(meteoro);
                palco.addActor(meteoro);


            }

        }
        float velocidade = 200; // velocidade de pixels por segundo
        for(Image meteoro :meteoros1){
            float x = meteoro.getX();
            float y = meteoro.getY() - velocidade * delta;
            meteoro.setPosition(x, y);
          // remove tiro sair da tela
            if (meteoro.getY() + meteoro.getHeight() < 0) {
                meteoro.remove(); //remove do palco
                meteoros1.removeValue(meteoro, true);
            }

        }
        float velocidade1 = 250; // velocidade de pixels por segundo
        for(Image meteoro :meteoros2) {
            float x = meteoro.getX();
            float y = meteoro.getY() - velocidade * delta;
            meteoro.setPosition(x, y);

            if (meteoro.getY() + meteoro.getHeight() < 0) { //atualiza posiçao do meteoro
                meteoro.remove(); //remove do palco
                meteoros2.removeValue(meteoro, true);
            }

        }

        }

    private final float MIN_INTERVALO_TIROS = 0.4f; // Minimo de tempo entre os tiros
    private float intervaloTiros = 0;// Tempo acumulados entre os tiros


    private void atualizarTiro(float delta) {
        intervaloTiros = intervaloTiros + delta;// Acumula o tempo percorrido
        if (atirando) {
            // Verifica  se o tempo minimo foi atingido
            if (intervaloTiros >= MIN_INTERVALO_TIROS) {
                Image tiro = new Image(texturaTiro);
                float x = jogador.getX() + jogador.getWidth() / 2 - tiro.getWidth() / 2;
                float y = jogador.getY() + jogador.getHeight();
                tiro.setPosition(x, y);
                tiros.add(tiro);
                palco.addActor(tiro);
                intervaloTiros = 0;
            }
        }
        float velocidade = 200;// pixels por segundo
        // Percorre a todo as lista de tiro na tela
        for (Image tiro : tiros) {
            //movimento o tiro em direção ao topo
            float x = tiro.getX();
            float y = tiro.getY() + velocidade * delta;
            tiro.setPosition(x, y);
            // remove os tiros que sairam da tela
            if (tiro.getY() > camera.viewportHeight) {
                tiros.removeValue(tiro, true);//remove da lista
                tiro.remove();//remove do palco

            }


        }
        for (Image tiro : tiros) {
            //movimento o tiro em direção ao topo
            float x = tiro.getX();
            float y = tiro.getY() + velocidade * delta;
            tiro.setPosition(x, y);
            // remove os tiros que sairam da tela
            if (tiro.getY() > camera.viewportHeight) {
                tiros.removeValue(tiro, true);//remove da lista
                tiro.remove();//remove do palco

            }


        }
    }

    /**
     * Atualiza a posi��o do jogador
     * @param delta
     */
    private void atualizarJogador(float delta) {
        float velocidade = 200;// velocidade de movimento do jogador
        if (indoDireta){
            //Verifica se o jogador esta dentro da tela
            if (jogador.getX() < camera.viewportWidth - jogador.getWidth()){
                float x  = jogador.getX() + velocidade * delta;
                float y  = jogador.getY() ;
                jogador.setPosition(x, y);

            }



        }
        if (indoEsquerda){
            //Verifica se o jogador esta dentro da tela
            if (jogador.getX() >= 0){


            float x  = jogador.getX() - velocidade * delta;
            float y  = jogador.getY() ;


                jogador.setPosition(x, y);

            }
        }
        if (indoDireta){
            //trocar imagem direta
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogadorDireta)));

        }else if (indoEsquerda){
            //trocar imagem esquerda
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogadorEsquerda)));

        }else  {
            //trocar imagem centro
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogador)));

        }

    }

    /**
     * Verifica se a teclas esta pressionadas
     */

    private void captureTeclas() {
        indoDireta   = false;
        indoEsquerda = false;
        atirando     = false;


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            indoEsquerda = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            indoDireta = true;
        }if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            atirando = true;
        }


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
        texturaJogador.dispose();
        texturaJogadorDireta.dispose();
        texturaJogadorEsquerda.dispose();
        texturaTiro.dispose();
        texturaMeteoro1.dispose();
        texturaMeteoro2.dispose();

    }
}
