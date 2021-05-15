kW/h coding assignment

The purpose of this assignment is to create a web form which allows to display and filter a users electricityconsumption history. It should also be possible to enter a kW/h price and use it to calculate the monetaryvalue of energy consumption


Requirement:
An Object Oriented approach should be used - the stated problem can be easily solved procedurally,but the main goal of this exercise is to assess your development ability and knowledge. It is highlyrecommended to use more classes and data structures, than strictly necessary to deliver a functioningsolution - we want to actually see you using OOP.

It is permitted but not mandatory to use a well known framework - server side and client side.
The solution should look aesthetic and presentable
On the form it should be possible to select a date range for which to show consumption history. Itshould only be possible to select those values for which there is data present (up to two years in thepast)
On the form it should be possible to select a period by which the data is grouped (days, weeks,months)

The form should include a field to enter the kW/h price. Upon entry the value should be used to on thefront-end to supplement each consumption history row with its monetary value

The download and aggregation of data should be done on the server side

The results must be cached so as not to repeat the same queries

The results are sent to the front-end in structural format using AJAX. Calculations, excluding the initialdata aggregation, which could be done on the client side, must not trigger server calls.

The data is presented to the user in a table according to the selected filters (it is not forbidden to usecharts)

All error situations should be handled and the user should be shown adequate error messages


start, the start date from which consumption information should be returned, in format dd-mm-yyyy
end, the end date until consumption information should be returned, in format dd-mm-yyyy

The consumption feed is a bit "broken" and always returns information for a longer period thanrequested
The response format of the feed is always XML


The consumption history can be queried up to two years in the past
The consumption history is presented as hourly data
kW/h coding assignment

The purpose of this assignment is to create a web form which allows to display and filter a users electricityconsumption history. It should also be possible to enter a kW/h price and use it to calculate the monetaryvalue of energy consumption.
