package juego.controladores;

import juego.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;

    public float r,g,b,a;


    private static Window window = null;

    private static Scene currentScene;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                //currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknow scene '" +newScene + "'";
                break;
        }
    }

    public static Window get() {
        if(Window.window == null){
            Window.window = new Window();
        }
        return  Window.window;
    }

    public void run() {
        System.out.println("Hola LWJGL " + Version.getVersion() + "!");
        init();
        loop();
    }

    private void loop() {

        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            //pool events
            glfwPollEvents();

            //color rojo: 1.0f,0.0f,0.0f,1.0f
            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >=0){
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;

//            //liberar la memoria
//            glfwFreeCallbacks(glfwWindow);
//            glfwDestroyWindow(glfwWindow);
//
//            //finalizar glfw y liberar el error callback
//
//            glfwTerminate();
//            glfwSetErrorCallback(null).free();
        }
    }

    private void init() {
        //llamada de error
        GLFWErrorCallback.createPrint(System.err).set();
        // Iniciar GLFW
        if (!glfwInit()){
            throw new IllegalStateException("No es posible iniciar GLFW.");
        }
        // Configurar GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_TRUE);

        //crear la ventana
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Falló al crear la ventana.");
        }

        //mouse callback
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallBack);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallbck);

        //teclado callback
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //hacer el contexto de opengl
        glfwMakeContextCurrent(glfwWindow);
        //habilita v-sync
        glfwSwapInterval(1);

        //hacer la ventana visible
        glfwShowWindow(glfwWindow);

        /*Esta linea es critica para LWJGL para la interconexion
         con el contexto GLFW de OpenGl, o cualquier contexto
         manejado de forma externa. LWJGL detecta el contexto
         que se encuentra en el hilo actual crea una instancia
         de GLCApabilities y permite el uso de bindings de OpenGL.*/
        GL.createCapabilities();

        Window.changeScene(0);
    }

}
