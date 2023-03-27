module Dreadbury2

abstract sig Person {
  killed: set Person
}

one sig Agatha, Butler, Charles extends Person {}

fact someoneKilledAgatha {
	some killed.Agatha
}

fact agathaKilled {
	Agatha in Person.killed
}

run {} for 3
