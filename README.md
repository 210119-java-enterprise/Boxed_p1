# Boxed_p1
A ORM that allows for a simplified and SQL-free interaction with the relational data source


# Use:
There are currently two client facing classes: BlackBox and QueryBuilder

BlackBox: 
  - Allows you to get a new connection and set currentConnection
  - Allows you to execute a query stored in a string
  - Allows you to get a ResultSet 'Summary' that shows what the raw result set holds
  - pending: retrieving an entity containing latest result set data
  
QueryBuilder:
  - Allows you to craft a SELECT statement through method chaining with no prior SQL knowledge
  - getQuery returns a string holding the completed query
  
Currently all other classes are intended for 'internal' use by BlackBox.
