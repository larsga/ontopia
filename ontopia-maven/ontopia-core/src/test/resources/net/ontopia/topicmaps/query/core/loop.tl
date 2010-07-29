
import "classpath:net/ontopia/topicmaps/query/core/loop.tl" as sub

grandchild($GF, $GM, $GC) :-
  parenthood($GF : father, $GM : mother, $C : child),
  parenthood($C : father, $M : mother, $GC : child).