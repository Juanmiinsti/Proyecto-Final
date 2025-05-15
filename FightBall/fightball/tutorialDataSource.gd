extends Node

var DBtutorials: Array[tutorialDB] = []
var getAllTutorials: HTTPRequest
var data_hash: int = 0
var already_loaded: bool = false

func _ready() -> void:
	if PlayerInfo.userKey.is_empty():
		PlayerInfo.session_ready.connect(_on_session_ready)
	else:
		_on_session_ready()

func _on_session_ready() -> void:
	print("🟢 PlayerInfo listo. Obteniendo tutoriales...")

	getAllTutorials = HTTPRequest.new()
	add_child(getAllTutorials)

	getAllTutorials.request_completed.connect(_on_get_all_tutorials_completed)

	var headers = [
		"Accept: application/json",
		"Authorization: Bearer " + PlayerInfo.userKey
	]

	var result = getAllTutorials.request(
		PlayerInfo.urlSpring + "/api/tutorials",
		headers,
		HTTPClient.METHOD_GET
	)

	if result != OK:
		print("❌ Error al iniciar la petición HTTP:", result)

func _on_get_all_tutorials_completed(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray) -> void:
	if response_code == 200:
		var json_string = body.get_string_from_utf8()
		var parsed_json = JSON.parse_string(json_string)
		var new_hash = hash(json_string)

		if already_loaded and new_hash == data_hash:
			print("🔁 Los tutoriales ya estaban cargados y no han cambiado.")
			return

		DBtutorials.clear()
		if parsed_json is Array and parsed_json.size() > 0:
			for tut in parsed_json:
				var new_tutorial = parse_tutorial(tut)
				if new_tutorial:
					DBtutorials.append(new_tutorial)
			print("✅ Tutoriales cargados:", DBtutorials.size())
		else:
			print("⚠️ La respuesta está vacía o mal formada")

		data_hash = new_hash
		already_loaded = true
	else:
		print("❌ Error HTTP", response_code)

func parse_tutorial(tut: Dictionary) -> tutorialDB:
	if not tut.has("title") or not tut.has("description"):
		print("⚠️ Datos de tutorial incompletos:", tut)
		return null

	var t = tutorialDB.new()
	t.title = tut["title"]
	t.description = tut["description"]
	return t
