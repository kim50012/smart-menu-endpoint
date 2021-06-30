package com.basoft.eorder.application.framework.spring;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.application.framework.CommandHandleEngine;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.DefaultCommandHandlerEngine;
import com.basoft.eorder.application.framework.MultiCommandHandler;

/**
 * AutoCommandHandler 注解处理器
 *
 * @author woonill lee
 * @link CommandHandler.AutoCommandHandler
 * @since 0.1
 */
@Slf4j
public abstract class SpringCommandHandleEngineConfigure implements BeanDefinitionRegistryPostProcessor {
    private Map<String, Class<? extends Command>> commandMap = new HashMap<>();

    private Map<String, FuncCommandHolder> funcCommandHanderMap = new HashMap<>();

    /********************************为CommandHandleEngine实例化做数据准备 START********************************/
    /**
     * 处理指定包下面的所有Annotation为CommandHandler.AutoCommandHandler的 Class（Method注解为CommandHandler.AutoCommandHandler）
     * 注册到 Map当中。供实例化CommandHandleEngine使用
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry bdr) throws BeansException {
        // System.out.println("\n\n\nCommand Initialization Step I>>>>>>Start Collecting Command Mapping<<<<<<<<<<");
//        log.info("\n\n\nCommand Initialization Step I>>>>>>Start Collecting Command Mapping<<<<<<<<<<");
        final Set<Class<?>> customClasses = SpringCommandHandleEngineConfigure.scanCommandComponents(getCommandScanPackage(), getSpringClassTypeFilter());
        customClasses
                .stream()
                .filter(new Predicate<Class<?>>() {
                    @Override
                    public boolean test(Class<?> aClass) {
                        return AnnotationUtils.findAnnotation(aClass, CommandHandler.AutoCommandHandler.class) != null;
                    }
                })
                .forEach(new Consumer<Class<?>>() {
                    @Override
                    public void accept(Class<?> aClass) {
                        final CommandHandler.AutoCommandHandler annotation = AnnotationUtils.findAnnotation(aClass, CommandHandler.AutoCommandHandler.class);
                        final Method[] declaredMethods = aClass.getDeclaredMethods();

                        for (Method m : declaredMethods) {
                            final CommandHandler.AutoCommandHandler handlerAnno = AnnotationUtils.getAnnotation(m, CommandHandler.AutoCommandHandler.class);
                            if (handlerAnno != null) {
                                final Class<?>[] parameterTypes = m.getParameterTypes();
                                if (parameterTypes != null && parameterTypes.length > 0) {
                                    boolean isFind = false;
                                    for (Class<?> parType : parameterTypes) {
                                        if (Command.class.isAssignableFrom(parType)) {
                                            // System.out.println("Start regist CommandMethod:" + m.getName() + "  on Class:" + aClass.getName());
//                                            log.info("Start regist CommandMethod:" + m.getName() + "  on Class:" + aClass.getName());
                                            Class<? extends Command> commandType = (Class<? extends Command>) parType;
                                            funcCommandHanderMap.put(m.getName(), new FuncCommandHolder(handlerAnno.value(), commandType, m, handlerAnno));
                                            commandMap.put(handlerAnno.value(), commandType);
                                            break;
                                        }
                                    }

                                    final FuncCommandHolder funcCommandHolder = funcCommandHanderMap.get(m.getName());
                                    if (funcCommandHolder == null) {
                                        throw new IllegalStateException("not found CommandHandler on:" + m.getName());
                                    }
                                } else {
                                    throw new IllegalArgumentException("AutoCommandHandler Method  and 1 element:" + m.getName());
                                }
                            }
                        }
                        bdr.registerBeanDefinition(annotation.value(), BeanDefinitionBuilder.genericBeanDefinition(aClass).getBeanDefinition());
                    }
                });
        // System.out.println("\n\n\nCommand Initialization Step I>>>>>>Collect Command Mapping Ended<<<<<<<<<<");
//        log.info("\n\n\nCommand Initialization Step I>>>>>>Collect Command Mapping Ended<<<<<<<<<<");
    }

    abstract protected String getCommandScanPackage();

    public static Set<Class<?>> scanCommandComponents(String packagePath, TypeFilter includeFilter) {
        boolean useDefaultFilters = false;//是否使用默认的filter，使用默认的filter意味着只扫描那些类上拥有Component、Service、Repository或Controller注解的类。
        ClassPathScanningCandidateComponentProvider beanScanner = new ClassPathScanningCandidateComponentProvider(useDefaultFilters);
        beanScanner.addIncludeFilter(includeFilter);
        Set<BeanDefinition> beanDefinitions = beanScanner.findCandidateComponents(packagePath);
        Set<Class<?>> commandClassSet = new HashSet<>();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            //beanName通常由对应的BeanNameGenerator来生成，比如Spring自带的AnnotationBeanNameGenerator、DefaultBeanNameGenerator等，也可以自己实现。
            String beanName = beanDefinition.getBeanClassName();
            try {
                final Class<?> aClass = ClassUtils.forName(beanName, ClassUtils.getDefaultClassLoader());
                commandClassSet.add(aClass);
            } catch (Exception e) {
                e.printStackTrace();
                //只有启动时才会用到此方法，因此抛出异常就停止启动
                throw new RuntimeException(e);
            }
        }
        return commandClassSet;
    }

    protected TypeFilter getSpringClassTypeFilter() {
        return new TypeFilter() {
            @Override
            public boolean match(
                    MetadataReader metadataReader,
                    MetadataReaderFactory metadataReaderFactory) throws IOException {
                return metadataReader.getClassMetadata().isConcrete();
            }
        };
    }

    static final class FuncCommandHolder {
        public final String name;
        public final Class<? extends Command> cmdClass;
        public final Method method;
        public final CommandHandler.AutoCommandHandler anno;

        public FuncCommandHolder(String value, Class<? extends Command> aClass, Method method1, CommandHandler.AutoCommandHandler handlerAnno) {
            this.name = value;
            this.cmdClass = aClass;
            this.method = method1;
            this.anno = handlerAnno;
        }
    }
    /********************************为CommandHandleEngine实例化做数据准备 END********************************/

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory clbf) throws BeansException {
        // final EOrderCommandHandler bean = clbf.getBean(EOrderCommandHandler.class);
        // System.out.println(bean.getClass().getName());
    }

    /********************************CommandHandleEngine实例化 START********************************/
    /**
     * 实例化Command Handle Engine
     *
     * @param appContext
     * @return
     */
    @Bean
    public CommandHandleEngine getEngine(ApplicationContext appContext) {
        // System.out.println("\n\n\nCommand Initialization Step II>>>>>>Start Command Handle Engine<<<<<<<<<<");
        log.info("\n\n\nCommand Initialization Step II>>>>>>Start Command Handle Engine<<<<<<<<<<");
        return new DefaultCommandHandlerEngine(this.commandMap, wrapCommand(getSpringCommandHandler(appContext), appContext));
    }

    /**
     * Wrap Command
     *
     * @param commandHandler
     * @param applicationContext
     * @return
     */
    protected CommandHandler<Command> wrapCommand(CommandHandler<Command> commandHandler, ApplicationContext applicationContext) {
        return commandHandler;
    }

    public CommandHandler<Command> getSpringCommandHandler(ApplicationContext appContext) {
        final List<Object> commandHanlers = new ArrayList<>();
        final Map<String, Object> beansWithAnnotation = appContext.getBeansWithAnnotation(CommandHandler.AutoCommandHandler.class);
        commandHanlers.addAll(beansWithAnnotation.values());
        return getCommandHandler(commandHanlers, this.funcCommandHanderMap);
    }

    protected CommandHandler<Command> getCommandHandler(List<Object> commandHanlers, Map<String, FuncCommandHolder> funcHolders) {
        return new SpringMultiCommandHandler(commandHanlers) {
            @Override
            protected AutoCommandHandler getMethodAnnotation2(Method m) {
                final FuncCommandHolder funcCommandHolder = funcHolders.get(m.getName());
                if (funcCommandHolder == null) {
                    // System.out.println("not found method :"+m.getName()+" anno");
                    return null;
                }
                // System.out.println("find anno success:" + funcCommandHolder.anno + " method:" + m.getName());
                log.info("find anno success:" + funcCommandHolder.anno + " method:" + m.getName());
                return funcCommandHolder.anno;
            }
        };
    }

    abstract static class SpringMultiCommandHandler extends MultiCommandHandler {
        public SpringMultiCommandHandler(List<Object> commandHanlers) {
            super(commandHanlers);
        }

        abstract protected AutoCommandHandler getMethodAnnotation2(Method m);

        @Override
        protected AutoCommandHandler getMethodAnnotation(Method m) {
            return getMethodAnnotation2(m);
        }
    }
    /********************************CommandHandleEngine实例化 END********************************/
}