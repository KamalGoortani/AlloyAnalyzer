module productPrice

sig Product {
    price: Int,
    discount: Int
}

fact Fact1 {
    all p: Product | p.discount >= 0 and p.discount <= p.price
}


fact Fact3 {
    all p1, p2: Product | p1.price = p2.price implies p1.discount = p2.discount
}
/*
In this model, we define a Product signature with two fields: price and discount,
 representing the original price and the discount applied to the product, respectively.
Fact1 states that the discount of every product must be greater than or equal to zero and less than or equal to the original price.
Fact2 is redundant and states that discount is less than or equal to the original price in diffeent formulation.
Fact3 stating that if two products have the same original price, then they must have the same discount. 
*/
