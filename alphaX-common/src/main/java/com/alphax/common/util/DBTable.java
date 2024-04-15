/**
 * 
 */
package com.alphax.common.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author A106744104
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DBTable {

	public String columnName();
	
	public boolean required();
	
}
