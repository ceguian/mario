package juego.controladores;

public class LevelScene extends Scene{
    public LevelScene(){
        System.out.println("inside leve scene");
        Window.get().r = 1;
        Window.get().g =1;
        Window.get().b =1;
    }

    @Override
    public void update(float dt) {

    }
}
