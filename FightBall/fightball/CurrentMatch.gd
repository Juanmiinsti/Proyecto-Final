extends Node
class_name currMatch
var firstTime:bool=true

var p1Victory=0
var p2Victory=0

var char_winner_id:int=2
var char_loser_id:int=1
var user_winner_id:int
var user_loser_id:int 
var lenght:float
var date:String

func sendMatch() -> void:
	var http = HTTPRequest.new()
	add_child(http)

	var url = "http://localhost:8080/api/match"  # Reemplazá con tu URL real

	var data = {
		"char_winner_id": char_winner_id,
		"char_loser_id": char_loser_id,
		"user_winner_id": user_winner_id,
		"user_loser_id": user_loser_id,
		"length": lenght,
		"date": date
	}

	var headers = [
		"Content-Type: application/json"
	]

	var json_data = JSON.stringify(data)
	var error = http.request(
		url,
		headers,
		HTTPClient.METHOD_POST,
		json_data
	)

	if error != OK:
		print("Error al enviar la petición:", error)
	else:
		print("Petición enviada")
