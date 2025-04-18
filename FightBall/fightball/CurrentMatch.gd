extends Node
class_name currMatch

var firstTime: bool = true

var p1Victory = 0
var p2Victory = 0

var char_winner_id: int
var char_loser_id: int
var user_winner_id: int
var user_loser_id: int
var lenght: float
var date: String

func _ready():
	# Ejemplo de cómo setear la fecha automáticamente al momento actual
	var now = Time.get_datetime_dict_from_system()
	date = "%04d-%02d-%02dT%02d:%02d:%02d" % [
		now.year, now.month, now.day,
		now.hour, now.minute, now.second
	]

func sendMatch() -> void:
	var http = HTTPRequest.new()
	add_child(http)

	# Conectamos para ver la respuesta del servidor
	http.request_completed.connect(_on_request_completed)

	var url = "http://localhost:8080/api/matches"

	# Usamos las claves exactamente como las espera el DTO en Spring
	var data = {
		"date": date,
		"length": lenght,
		"charWinner": char_winner_id,
		"charLoser": char_loser_id,
		"userWinner": user_winner_id,
		"userLoser": user_loser_id
	}

	var headers = [
		"Content-Type: application/json"
	]

	var json_data = JSON.stringify(data)
	print("JSON enviado:", json_data)  # Log para debug

	var error = http.request(
		url,
		headers,
		HTTPClient.METHOD_POST,
		json_data
	)

	if error != OK:
		print("Error al enviar la petición:", error)
	else:
		print("Petición enviada correctamente")

func _on_request_completed(result, response_code, headers, body):
	print("Código de respuesta:", response_code)
	print("Respuesta del servidor:", body.get_string_from_utf8())
