package net.me.daemon.particles.api.type;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.me.daemon.particles.api.register.DaemonShaderRegistry;
import net.me.daemon.particles.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DaemonParticleType {

    public Vec3d position, velocity, gravity;
    public Identifier id;
    public int lifetime;
    public List<Identifier> textures;
    public MatrixStack matrixStack = new MatrixStack();
    public static MinecraftClient client;

    public DaemonParticleType(Identifier identifier) throws IOException {
        Utils.ParticleData data = Utils.loadParticleFromJSON(identifier);
        id = identifier;
        client = MinecraftClient.getInstance();

        position = data.position;
        velocity = data.velocity;
        gravity = data.gravity;
        lifetime = data.lifetime;
        textures = data.textures;

    }

    ///  updates view and projectiom matrix
    public void update_uniforms(WorldRenderContext context, Identifier shader) {
        ShaderProgram program = DaemonShaderRegistry.get(shader);

        Objects.requireNonNull(program.getUniform("ProjMat")).set(RenderSystem.getProjectionMatrix());


        // TODO: check if correct
        // if not use just use   RenderSystem.getModelViewMatrix()  until actual fix
        Objects.requireNonNull(program.getUniform("ModelViewMat")).set(context.positionMatrix());
    }

    public void set_texture(int index) {
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        client.getTextureManager().bindTexture(textures.get(index));
    }

    /// 0 = screen texture   1 = depth texture
    public void set_screen_texture(int type) {
        MinecraftClient client = MinecraftClient.getInstance();
        Framebuffer buffer = client.getFramebuffer();
        int texID;

        switch (type) {
            case 0 -> texID = buffer.getColorAttachment();
            case 1 -> texID = buffer.getDepthAttachment();
            default -> texID = buffer.getColorAttachment();
        }


        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        RenderSystem.bindTexture(texID);
    }

    public void setPosition(Vec3d pos) {
        this.position = pos;
    }

    // needs to be called before render since here u should call update_uniforms
    public abstract void tick(WorldRenderContext context);

    /// u need to define a custom shader in DaemonShaderRegistry and then get it from there to use it for rendering in here
    public abstract void render(WorldRenderContext context);

    public boolean markedForDeath() {
        return lifetime <= 0;
    }

    public void TrueUpdate(WorldRenderContext context) {

        // stops position and lifetime updates
        if(!client.isPaused()) {
            position = position.subtract(gravity);
            lifetime = lifetime - 1;
        }

        tick(context);
    }

    public void TrueRender(WorldRenderContext context) {
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        render(context);
        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
    }


















































}