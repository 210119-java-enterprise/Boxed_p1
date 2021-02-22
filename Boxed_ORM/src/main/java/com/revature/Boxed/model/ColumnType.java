package com.revature.Boxed.model;

/**
 * Differentiates between the diferent SQL constraints supported:
 * <ul>
 *     <li>PK : Primary Key </li>
 *     <li>FK : Foreign Key</li>
 *     <li>Default : none of the above</li>
 * </ul>
 */
public enum ColumnType {
    PK, FK, DEFAULT
}
