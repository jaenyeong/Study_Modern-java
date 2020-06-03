package com.jaenyeong.appendix.A;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface Authors {
	Author[] value();
}
