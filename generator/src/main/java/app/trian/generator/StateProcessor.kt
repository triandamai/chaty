package app.trian.generator

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.jvm.jvmOverloads
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import kotlin.reflect.KClass

class StateProcessor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val findStateClass = resolver.finAnnotation(State::class)
            .toList()

        findStateClass.forEach {
            val generatorFile = FileSpec
                .builder(
                    packageName = it.packageName.asString(),
                    fileName = it.simpleName.asString().plus("Impl")
                )
                .addFileComment("State generator by Trian Damai<triandamai@gmail.com> \n")
                .addFileComment("Generate from `${it.packageName.asString()}.${it.simpleName.asString()}`, do not edit!")

            val baseImpl = ClassName("app.trian.rust.common", "ImplClass")
            val currentState = ClassName(it.packageName.asString(), it.simpleName.asString())
            val interfaceCallback =
                ClassName(it.packageName.asString(), "${baseImpl.simpleName}.OnImplListener")
            val callbackName = "source"
            val setterParameterName = "value"
            val suffixImplName = "Impl"


            val typeSpec = TypeSpec.classBuilder(it.simpleName.asString().plus(suffixImplName))
                .addSuperinterface(baseImpl.plusParameter(currentState))

            typeSpec.addProperty(
                PropertySpec.builder(callbackName, interfaceCallback.plusParameter(currentState))
                    .mutable()
                    .addModifiers(KModifier.PRIVATE)
                    .initializer(
                        CodeBlock.builder()
                            .addStatement(
                                "object : %T {",
                                interfaceCallback.plusParameter(currentState)
                            )
                            .addStatement(
                                "override fun currentState(): %T =%T()",
                                currentState,
                                currentState
                            )
                            .addStatement("override fun invalidate(state: %T){}", currentState)
                            .addStatement("}")
                            .build()
                    )
                    .build()
            )

            typeSpec.addFunction(
                FunSpec.builder("default")
                    .addModifiers(KModifier.OVERRIDE)
                    .returns(currentState)
                    .addStatement("return ${it.simpleName.asString()}()")
                    .build()
            )
            typeSpec.addFunction(
                FunSpec
                    .builder("setCb")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter(
                        ParameterSpec(
                            name = "listener",
                            interfaceCallback.plusParameter(currentState)
                        )
                    )
                    .addStatement("this.${callbackName} = listener")
                    .build()
            )

            it.getDeclaredProperties().forEach { props ->
                val typeBuilder = PropertySpec
                    .builder(props.simpleName.asString(), props.type.toTypeName())
                    .mutable()
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return this.${callbackName}.currentState().${props.simpleName.asString()}")
                            .build()
                    )
                    .setter(
                        FunSpec.setterBuilder()
                            .addParameter(setterParameterName, props.type.toTypeName())
                            .addStatement("this.${callbackName}.invalidate(${callbackName}.currentState().copy(${props.simpleName.asString()} = ${setterParameterName}))")
                            .build()
                    )
                    .build()

                typeSpec.addProperty(typeBuilder)
            }
            //create class
            generatorFile.addType(typeSpec.build())
            generatorFile.build()
                .writeTo(
                    environment.codeGenerator,
                    Dependencies(
                        false,
                        *arrayOf()
                    )
                )
        }



        return findStateClass.filterNot { it.validate() }.toList()
    }
}

private fun Resolver.finAnnotation(
    kClass: KClass<*>,
) = getSymbolsWithAnnotation(kClass.qualifiedName.toString())
    .filterIsInstance<KSClassDeclaration>().filter {
        it.classKind == ClassKind.CLASS
    }