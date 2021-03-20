Current Design (03/19/2021)

1. Allows the user to put in a symbol, but does not group orders by symbol -- will need to fix later
2. Each book (asks, bids) act as their own standalone engines -- they contain all the methods and values need to perform the calculations
3. Current functionality:
    * Add order
    * Delete order (not cancel)
    * Semi match order
    
4. Order will stay on cache until it is completely filled
5. Order should never be removed from database

Current Design (03/20/2021)

1. Validates orders
    * Makes sure all values are valid
    * The symbol must be 'spx' for now
2. Order matching now works on full orders only. If it does not match the order instantly, it just adds it to the respective book
    * Partial order filling will be done tomorrow
3. Added a performance testing file
    * It seems that when it constantly gets matched, (e.g. One buy comes in and one sell comes in right after another) -- it is extremely fast.
    * However, if only nothing gets matched and one side gets built up, then it dramatically slows down (1 million for both took 79 seconds)
    * Need to figure out the bottleneck or what can be done to improve that speed
