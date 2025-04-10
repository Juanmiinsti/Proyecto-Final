extends Node
class_name currMatch
var firstTime:bool=true

var p1Victory=0
var p2Victory=0

var char_winner_id:int
var char_loser_id:int
var user_winner_id:int
var user_loser_id:int 
var lenght:float
var date:String

func sendMatch() -> void:
	var http = HTTPRequest.new()
	add_child(http)

	var url = "http://localhost:8080/api/matches"  # Reemplazá con tu URL real

	var data = {
		"charWinner": char_winner_id,
		"charLoser": char_loser_id,
		"userWinner": user_winner_id,
		"userLoser": user_loser_id,
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
