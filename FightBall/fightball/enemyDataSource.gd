extends Node

var DBenemies: Array[EnemyDB] = []
var getAllEnemies: HTTPRequest
var data_hash: int = 0
var already_loaded: bool = false

func _ready() -> void:
	if PlayerInfo.userKey.is_empty():
		PlayerInfo.session_ready.connect(_on_session_ready)
	else:
		_on_session_ready()

func _on_session_ready() -> void:
	print("🟢 PlayerInfo listo. Obteniendo enemigos...")

	getAllEnemies = HTTPRequest.new()
	add_child(getAllEnemies)

	getAllEnemies.request_completed.connect(_on_get_all_enemies_request_completed)

	var headers = [
		"Accept: application/json",
		"Authorization: Bearer " + PlayerInfo.userKey
	]

	var result = getAllEnemies.request(
		PlayerInfo.urlSpring + "/api/enemies",
		headers,
		HTTPClient.METHOD_GET
	)

	if result != OK:
		print("❌ Error al iniciar la petición HTTP:", result)

func _on_get_all_enemies_request_completed(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray) -> void:
	if response_code == 200:
		var json_string = body.get_string_from_utf8()
		var parsed_json = JSON.parse_string(json_string)
		var new_hash = hash(json_string)

		if already_loaded and new_hash == data_hash:
			print("🔁 Los enemigos ya estaban cargados y no han cambiado.")
			return

		DBenemies.clear()
		if parsed_json is Array and parsed_json.size() > 0:
			for enemy_data in parsed_json:
				var new_enemy = parse_enemy(enemy_data)
				if new_enemy:
					DBenemies.append(new_enemy)
			print("✅ Enemigos cargados:", DBenemies.size())
		else:
			print("⚠️ La respuesta está vacía o mal formada")

		data_hash = new_hash
		already_loaded = true
	else:
		print("❌ Error HTTP", response_code)

func parse_enemy(enemy: Dictionary) -> EnemyDB:
	if not enemy.has("name") or not enemy.has("max_health"):
		print("⚠️ Datos de enemigo incompletos:", enemy)
		return null

	var e = EnemyDB.new()
	e.naame = enemy["name"]
	e.max_health = enemy["max_health"]
	e.damage = enemy["damage"]
	return e
