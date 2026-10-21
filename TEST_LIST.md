The assignment sets a minimum of six tests for PolicyRepositoryTest. Write at least one test for each of these:

Each policy type keeps its type and field. A car, a bike and a truck policy are each saved and read back as their own type, with the field specific to that type. (One test or three? You decide, and be able to say why.)
Status change survives a reload. Write one test where an expired policy is still expired after being read back. Write a second where that read-back policy still refuses renew().
Status is stored as text. How will you check the raw column value rather than the Java enum? Hint: think about how to query the table directly.
The customer-name query returns exactly that customer's policies. Your test data needs a second customer so you can show their policies are excluded.
The expiry-range query works at both boundary dates. Write down which dates you'll test: on the start date, on the end date, one day before the start, and one day after the end.
Duplicate policy number saved through the repository. Write down what you predict will happen. Is it an exception, or does the second policy silently overwrite the first? Your test will check whether you were right.