TODO

Phase One
1. Allow an order to be placed
2. Allow an order to be matched based on symbol (only full order matching)
3. Allow partial matching based on symbol
4. Have exchange only run a specific amount of time -- after time, all orders get cancelled

Phase Two
1. Add in Price to order

Order:
- ID          (auto increment -- integer)
- Symbol      (varchar(255))
- Quantity    (integer)
