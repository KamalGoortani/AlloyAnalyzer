/* 
In this model, we have two signatures: Person and Car. Each Car has an owner who is a Person. 
The first fact, OwnerIsPerson, states that every Car has an owner who is a Person.
The second fact, PersonIsPerson, simply states that every Person is a Person.
so all facts are redundants from sig we know that owener is person.
 */

sig Person {}

sig Car {
  owner: Person
}

fact OwnerIsPerson {
  all c: Car | c.owner in Person
}

fact PersonIsPerson {
  all p: Person | p in Person
}