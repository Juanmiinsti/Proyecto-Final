extends Node
class_name currMatch

var firstTime: bool = true

var p1Victory = 0
var p2Victory = 0

var char_winner_id: int
var char_loser_id: int
var user_winner_id: int
var user_loser_id: int
var length: float = 0.0  # ⏱️ Duración de la partida en segundos
var date: String

var _start_time: int  # 🕒 Guardaremos el tiempo de inicio en milisegundos

func start_timer():
	_start_time = Time.get_ticks_msec()

	# También actualizamos la fecha de inicio
	var now = Time.get_datetime_dict_from_system()
	date = "%04d-%02d-%02dT%02d:%02d:%02d" % [
		now.year, now.month, now.day,
		now.hour, now.minute, now.second
	]

	print("🕒 Timer de partida iniciado:", date)


func sendMatch() -> void:
	# Calculamos la duración total de la partida
	var end_time = Time.get_ticks_msec()
	length = float(end_time - _start_time) / 1000.0  # lo pasamos a segundos

	print("📏 Duración de la partida:", length, "segundos")

	# Validamos que haya token antes de mandar
	if PlayerInfo.userKey.is_empty():
		print("❌ No hay token de usuario, cancelando envío de partida.")
		return

	# Creamos el nodo HTTPRequest y conectamos la señal
	var http = HTTPRequest.new()
	add_child(http)
	http.request_completed.connect(_on_request_completed)

	var url = "http://localhost:8080/api/matches"

	# Armamos el cuerpo de la petición como espera el backend
	var data = {
		"date": date,
		"length": length,
		"charWinner": char_winner_id,
		"charLoser": char_loser_id,
		"userWinner": user_winner_id,
		"userLoser": user_loser_id
	}

	var headers = [
		"Content-Type: application/json",
		"Authorization: Bearer " + PlayerInfo.userKey  # ✅ Token incluido
	]

	var json_data = JSON.stringify(data)
	print("📦 JSON enviado:", json_data)

	var error = http.request(url, headers, HTTPClient.METHOD_POST, json_data)
	if error != OK:
		print("❌ Error al enviar la petición:", error)
	else:
		print("📨 Petición enviada correctamente")

func _on_request_completed(result, response_code, headers, body):
	print("📬 Código de respuesta:", response_code)
	print("📝 Respuesta del servidor:", body.get_string_from_utf8())
