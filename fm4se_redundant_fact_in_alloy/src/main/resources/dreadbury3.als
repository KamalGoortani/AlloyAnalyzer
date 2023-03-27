module Dreadbury3

abstract sig Person {
  killed: set Person,
  hates: set Person,
  richer: set Person
}

one sig Agatha, Butler, Charles extends Person {}

pred nativeEncoding() {
// following fact is redundact because in line 20 fact state the same
  Agatha in Person.killed
  killed in hates and no (killed & richer)
  no (Charles.hates & Agatha.hates)
  (Person - Butler) in Agatha.hates
  (Person - richer.Agatha) in Butler.hates
  Agatha.hates in Butler.hates
  all x : Person | Person != x.hates
  some killed.Agatha  
}

run nativeEncoding for 3
