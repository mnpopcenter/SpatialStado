//
// Generated by JTB 1.2.2
//

package org.postgresql.stado.parser.core.syntaxtree;

/**
 * Grammar production:
 * f0 -> <INSERT_>
 * f1 -> [ <INTO_> ]
 * f2 -> TableName(prn)
 * f3 -> [ <PARENTHESIS_START_> ColumnNameList(prn) <PARENTHESIS_CLOSE_> ]
 * f4 -> ( <VALUES_> <PARENTHESIS_START_> SQLExpressionList(prn) <PARENTHESIS_CLOSE_> | SelectWithoutOrderWithParenthesis(prn) )
 */
public class InsertTable implements Node {
   public NodeToken f0;
   public NodeOptional f1;
   public TableName f2;
   public NodeOptional f3;
   public NodeChoice f4;

   public InsertTable(NodeToken n0, NodeOptional n1, TableName n2, NodeOptional n3, NodeChoice n4) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
      f3 = n3;
      f4 = n4;
   }

   public InsertTable(NodeOptional n0, TableName n1, NodeOptional n2, NodeChoice n3) {
      f0 = new NodeToken("INSERT");
      f1 = n0;
      f2 = n1;
      f3 = n2;
      f4 = n3;
   }

   public void accept(org.postgresql.stado.parser.core.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(org.postgresql.stado.parser.core.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}
