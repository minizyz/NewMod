# NewMod

一个基于 **Fabric** 的 Minecraft Java 版模组，目标版本 **1.21.1**。

## 当前进度

- [x] Fabric 1.21.1 模组工程骨架（Loom + Fabric Loader + Fabric API）
- [x] 注册一个空生物 `empty_entity`（`EmptyEntity`）
  - 暂未赋予 AI：`registerGoals()` 留空，未注册任何行为目标
  - 暂未赋予材质：渲染器仅返回预留纹理路径，贴图文件后续补充
- [x] 自定义史莱姆 `mod_slime`（`ModSlime`）
  - 直接继承原版 `Slime`，AI（跳跃移动、随机跳、攻击、死亡分裂等）全部沿用父类实现
  - 渲染复用原版史莱姆模型与纹理，可用 `/summon newmod:mod_slime` 生成
- [ ] 生物模型与贴图
- [ ] 刷怪蛋 / 生成规则 / 战利品表

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
└── src/main/
    ├── resources/fabric.mod.json                        # 模组元数据与入口点
    └── java/com/example/newmod/
        ├── NewMod.java                                  # 主入口（ModInitializer）
        ├── entity/
        │   ├── EmptyEntity.java                         # 空生物实体类
        │   ├── ModSlime.java                            # 自定义史莱姆（继承原版 Slime）
        │   └── ModEntities.java                         # 实体类型与属性注册
        └── client/
            ├── NewModClient.java                        # 客户端入口（ClientModInitializer）
            ├── EmptyEntityRenderer.java                 # 占位渲染器
            └── ModSlimeRenderer.java                    # 史莱姆渲染器（复用原版模型/纹理）
```

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
