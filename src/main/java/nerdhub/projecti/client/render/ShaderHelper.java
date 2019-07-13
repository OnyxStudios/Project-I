package nerdhub.projecti.client.render;

import com.mojang.blaze3d.platform.GLX;
import nerdhub.projecti.ProjectI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ShaderHelper {

    private static final int VERT = GLX.GL_VERTEX_SHADER;
    private static final int FRAG = GLX.GL_FRAGMENT_SHADER;

    public static final ResourceLocation BLOOM_SHADER_LOC = new ResourceLocation(ProjectI.MODID, "shaders/bloom.frag");

    public static int bloomShader = loadFragmentShaderProgram(BLOOM_SHADER_LOC);

    public static int loadShaderProgram(ResourceLocation vshID, ResourceLocation fshID) {
        int vertexShader = createShader(vshID, VERT);
        int fragmentShader = createShader(fshID, FRAG);
        int program = GLX.glCreateProgram();
        GLX.glAttachShader(program, vertexShader);
        GLX.glAttachShader(program, fragmentShader);
        GLX.glLinkProgram(program);

        return program;
    }

    public static int loadVertexShaderProgram(ResourceLocation vshID) {
        int vertexShader = createShader(vshID, VERT);
        int program = GLX.glCreateProgram();
        GLX.glAttachShader(program, vertexShader);
        GLX.glLinkProgram(program);

        return program;
    }

    public static int loadFragmentShaderProgram(ResourceLocation fshID) {
        int fragmentShader = createShader(fshID, FRAG);
        int program = GLX.glCreateProgram();
        GLX.glAttachShader(program, fragmentShader);
        GLX.glLinkProgram(program);

        return program;
    }

    public static int createShader(ResourceLocation shaderFile, int shaderType) {
        int shader = GLX.glCreateShader(shaderType);

        if(shader == 0) {
            return 0;
        }

        try {
            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(shaderFile));
        }catch (Exception e) {
            e.printStackTrace();
        }

        GLX.glCompileShader(shader);

        if(GL20.glGetShaderi(shader, GLX.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
        }

        return shader;
    }

    public static void useShader(int shader, Consumer<Integer> callback) {
        ARBShaderObjects.glUseProgramObjectARB(shader);

        if(shader != 0 && callback != null) {
            int ticks = ARBShaderObjects.glGetUniformLocationARB(shader, "ticks");
            ARBShaderObjects.glUniform1iARB(ticks, ClientTickHandler.ticksInGame);

            callback.accept(shader);
        }
    }

    public static void useShader(int shader) {
        useShader(shader, null);
    }

    public static void releaseShader() {
        useShader(0);
    }

    public static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public static String readFileAsString(ResourceLocation shaderFile) {
        ProjectI.LOGGER.info("Loading shader file " + shaderFile.toString());

        InputStream in = getShaderFile(shaderFile);
        String s = "";

        if (in != null){
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
                s = reader.lines().collect(Collectors.joining("\n"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ProjectI.LOGGER.fatal("Unable to parse shader file! Source: " + shaderFile.toString(), e);
            } catch (IOException e) {
                e.printStackTrace();
                ProjectI.LOGGER.fatal("Unable to parse shader file! Source: " + shaderFile.toString(), e);
            }
        }

        return s;
    }

    /**
     * Grab the shader file from the assets dir
     * @param shaderFile - The identifier leading to the file
     * @return - The InputStream for the shader file
     */
    public static InputStream getShaderFile(ResourceLocation shaderFile) {
        if(Minecraft.getInstance().getResourceManager().hasResource(shaderFile)) {
            try {
                return Minecraft.getInstance().getResourceManager().getResource(shaderFile).getInputStream();
            } catch (IOException e) {
                ProjectI.LOGGER.fatal("Unable to parse shader file! Source: " + shaderFile.toString(), e);
                return null;
            }
        }else {
            ProjectI.LOGGER.fatal("Unable to find shader file! Source: " + shaderFile.toString());
            return null;
        }
    }
}