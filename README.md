# NewMod

一个基于 **Fabric** 的 Minecraft Java 版模组，目标版本 **1.21.1**。

## 当前进度

- [x] Fabric 1.21.1 模组工程骨架（Loom + Fabric Loader + Fabric API）
- [x] 注册一个空生物 `empty_entity`（`EmptyEntity`）
  - 暂未赋予 AI：`registerGoals()` 留空，未注册任何行为目标
  - 暂未赋予材质：渲染器仅返回预留纹理路径，贴图文件后续补充
- [x] 自定义史莱姆 `mod_slime`（`ModSlime`，完全独立于原版命名空间）
  - 实体 ID `newmod:mod_slime`，模型层 `newmod:mod_slime`（main / outer），贴图 `newmod:textures/entity/mod_slime.png`
  - 行为主体沿用原版 `Slime` 的 AI：跳跃移动、随机跳、挤压音效与粒子、死亡分裂
  - 粘液球驯服：每次成功率为 30%，第 5 次尝试必定成功（每次尝试消耗 1 个粘液球）
  - 驯服后不主动攻击玩家；驯服者（含其射出的箭/投掷物）无法伤害它
  - 驯服者被攻击时，史莱姆会主动锁定攻击者并反击（32 格内）
  - 已驯服的史莱姆不会在和平难度下被清除
- [x] 刷怪蛋 `newmod:mod_slime_spawn_egg`，已加入创造模式“刷怪蛋”分类
- [ ] 更多生物与材质

## 环境要求

- JDK 25（Loom 1.19.x 在 JVM 25 上运行；模组字节码通过 release 21 编译）
- Minecraft Java 1.21.1
- Fabric Loader 0.20.1+
- Fabric API（对应 1.21.1）

## 工程结构

```
NewMod/
├── build.gradle / settings.gradle / gradle.properties   # Gradle + Loom 构建配置
├── gradle/wrapper/                                      # Gradle Wrapper
├── textures_b64/                                        # 贴图的 base64 文本（构建时解码）
└── src/main/
    ├── resources/
    │   ├── fabric.mod.json                              # 模组元数据与入口点
    │   └── assets/newmod/                               # 模型、语言文件
    └── java/com/example/newmod/
        ├── NewMod.java                                  # 主入口（ModInitializer）
        ├── entity/
        │   ├── EmptyEntity.java                         # 空生物实体类
        │   ├── ModSlime.java                            # 自定义史莱姆（驯服/反击逻辑）
        │   └── ModEntities.java                         # 实体类型与属性注册
        ├── item/
        │   └── ModItems.java                            # 刷怪蛋等物品注册
        └── client/
            ├── NewModClient.java                        # 客户端入口（ClientModInitializer）
            ├── EmptyEntityRenderer.java                 # 占位渲染器
            ├── ModSlimeRenderer.java                    # 史莱姆渲染器（独立模型层/贴图）
            └── ModSlimeOuterLayer.java                  # 史莱姆外层果冻
```

> 贴图以 base64 文本存放在 `textures_b64/`，`gradle build` 时会自动解码到
> `build/generated/modTextures` 并打进 jar（仓库只保存文本，方便纯文本推送）。

## 构建

需要本地安装 **Gradle 9.8.2+**（版本见 `gradle/wrapper/gradle-wrapper.properties`）与 JDK 25。

首次可在项目根目录生成 Wrapper 脚本与 jar：

```bash
gradle wrapper
```

之后即可使用 Wrapper：

```bash
# 开发环境（自动安装客户端到 run/ 目录）
./gradlew runClient

# 构建产物 jar（输出于 build/libs/）
./gradlew build
```

也可以直接使用本地 Gradle：

```bash
gradle runClient
gradle build
```

## 版本信息

| 组件            | 版本                |
| --------------- | ------------------- |
| Minecraft       | 1.21.1              |
| Fabric Loader   | 0.20.1              |
| Fabric API      | 0.116.18+1.21.1     |
| Loom            | 1.19.4              |
| Java（构建运行）| 25                  |
| Java（编译目标）| 21                  |

## 许可证

[CC0-1.0](LICENSE)
