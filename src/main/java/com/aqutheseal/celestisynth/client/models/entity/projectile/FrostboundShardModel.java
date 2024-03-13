package com.aqutheseal.celestisynth.client.models.entity.projectile;

// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.aqutheseal.celestisynth.Celestisynth;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class FrostboundShardModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Celestisynth.prefix("frostbound_shard_model"), "main");
	private final ModelPart shard;

	public FrostboundShardModel(ModelPart root) {
		this.shard = root.getChild("shard");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition shard = partdefinition.addOrReplaceChild("shard", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = shard.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 15).mirror().addBox(-1.0F, -10.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, -1.5708F, 0.7854F, -1.5708F));

		PartDefinition cube_r2 = shard.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 15).addBox(-3.0F, -10.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 1.5708F, 0.7854F, 1.5708F));

		PartDefinition cube_r3 = shard.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 15).mirror().addBox(-1.0F, -10.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r4 = shard.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 15).addBox(-3.0F, -10.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		shard.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}