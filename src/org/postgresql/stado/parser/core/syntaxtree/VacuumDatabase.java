//
// Generated by JTB 1.2.2
//

package org.postgresql.stado.parser.core.syntaxtree;

/**
 * Grammar production:
 * f0 -> <VACUUM_>
 * f1 -> [ <FULL_> | <FREEZE_> ]
 * f2 -> [ TableName(prn) | AnalyzeDatabase(prn) ]
 */
public class VacuumDatabase implements Node {
   public NodeToken f0;
   public NodeOptional f1;
   public NodeOptional f2;

   public VacuumDatabase(NodeToken n0, NodeOptional n1, NodeOptional n2) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
   }

   public VacuumDatabase(NodeOptional n0, NodeOptional n1) {
      f0 = new NodeToken("VACUUM");
      f1 = n0;
      f2 = n1;
   }

   public void accept(org.postgresql.stado.parser.core.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(org.postgresql.stado.parser.core.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}
