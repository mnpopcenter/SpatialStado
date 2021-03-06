//
// Generated by JTB 1.2.2
//

package org.postgresql.stado.parser.core.syntaxtree;

/**
 * Grammar production:
 * f0 -> <INT_LITERAL>
 * f1 -> <PLACEMENT_>
 * f2 -> ( <ALL_> | <ROUND_ROBIN_> )
 * f3 -> <PSCHEME_>
 * f4 -> Identifier(prn)
 */
public class SpatialPartitionOptions implements Node {
   public NodeToken f0;
   public NodeToken f1;
   public NodeChoice f2;
   public NodeToken f3;
   public Identifier f4;

   public SpatialPartitionOptions(NodeToken n0, NodeToken n1, NodeChoice n2, NodeToken n3, Identifier n4) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
      f3 = n3;
      f4 = n4;
   }

   public SpatialPartitionOptions(NodeToken n0, NodeChoice n1, Identifier n2) {
      f0 = n0;
      f1 = new NodeToken("PLACEMENT");
      f2 = n1;
      f3 = new NodeToken("PSCHEME");
      f4 = n2;
   }

   public void accept(org.postgresql.stado.parser.core.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(org.postgresql.stado.parser.core.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

