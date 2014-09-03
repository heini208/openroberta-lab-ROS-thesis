package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all statements. There are two ways for a client to find out which kind of statement an {@link #Stmt}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Stmt<V> extends Phrase<V> {
    /**
     * This constructor set the kind of the statement object used in the AST (abstract syntax tree). All possible kinds can be found in {@link Kind}.
     * 
     * @param kind of the the statement object used in AST,
     * @param disabled is the block,
     * @param comment of the user for the specific block
     */
    public Stmt(Kind kind, boolean disabled, String comment) {
        super(kind, disabled, comment);
    }

}