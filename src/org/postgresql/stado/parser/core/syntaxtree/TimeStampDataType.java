//
// Generated by JTB 1.2.2
//

package org.postgresql.stado.parser.core.syntaxtree;

/**
 * Grammar production:
 * f0 -> <TIMESTAMP_> [ LengthSpec() ] [ ( <WITH_TIMEZONE_> | <WITHOUT_TIMEZONE_> ) ]
 *       | <DATETIME_>
 *       | <SAMLLDATETIME_>
 */
public class TimeStampDataType implements Node {
   public NodeChoice f0;

   public TimeStampDataType(NodeChoice n0) {
      f0 = n0;
   }

   public void accept(org.postgresql.stado.parser.core.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(org.postgresql.stado.parser.core.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}
