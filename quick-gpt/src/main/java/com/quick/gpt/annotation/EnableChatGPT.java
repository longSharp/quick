package com.quick.gpt.annotation;

import com.quick.gpt.config.ChatGPTAutoConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ChatGPTAutoConfigure.class)
@Inherited
public @interface EnableChatGPT {
}
