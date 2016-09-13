//
// Generated by JTB 1.2.2
//

package org.postgresql.stado.parser.core.syntaxtree;

/**
 * Grammar production:
 * f0 -> ( <USER_> | <CURRENT_USER_> )
 * f1 -> [ <PARENTHESIS_START_> <PARENTHESIS_CLOSE_> ]
 */
public class Func_User implements Node {
   public NodeChoice f0;
   public NodeOptional f1;

   public Func_User(NodeChoice n0, NodeOptional n1) {
      f0 = n0;
      f1 = n1;
   }

   public void accept(org.postgresql.stado.parser.core.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(org.postgresql.stado.parser.core.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}
