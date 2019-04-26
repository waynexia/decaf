package inter;

import symbols.Type;

public class For extends Stmt {

   Expr expr_judge; Stmt expr_begin,expr_end,stmt;

   public For(){
       expr_begin = null;
       expr_judge = null;
       expr_end = null;
       stmt = null;
    }

   public void init(Stmt s1, Expr x, Stmt s2, Stmt s) {
      expr_begin = s1;
      expr_judge = x;
      expr_end = s2;
      stmt = s;
      if( expr_judge.type != Type.Bool ) expr_judge.error("boolean required in for(;`THERE`;)");
   }

   public void gen(int b, int a) {}
   
   public void display(){
	   emit("stmt : for begin");
	   //expr
       expr_begin.display();
       //expr_judge.display();
       expr_end.display();
       emit("stmt : for body");
       stmt.display();
	   emit("stmt : for end");
   }
}
/*
test code:
{
    int i;int j;float v;float x;float[100] a;
    while(true){
        do i = i + 1;while(a[i]<v);
        do j = j + 1;while(a[j]>v);
        if(i>=j)break;
        x=a[i];a[i]=a[j];a[j]=x;
    }
    for(x=a[i] ; i<10 ; i = i + 1;){
        a[j]=x;
    }
}
*/