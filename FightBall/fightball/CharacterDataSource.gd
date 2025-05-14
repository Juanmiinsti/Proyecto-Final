extends Node

var DBcharacters: Array[CharacterDB] = []
var getAllCharacter: HTTPRequest
var data_hash: int = 0
var already_loaded: bool = false

func _ready() -> void:
	if PlayerInfo.userKey.is_empty():
		PlayerInfo.session_ready.connect(_on_session_ready)
	else:
		_on_session_ready()

func _on_session_ready() -> void:
	print("🟢 PlayerInfo listo. Obteniendo personajes...")
	print(PlayerInfo.userName)
	print(PlayerInfo.userPassword)
	print(PlayerInfo.userKey)

	getAllCharacter = HTTPRequest.new()
	add_child(getAllCharacter)

	getAllCharacter.request_completed.connect(_on_get_all_request_request_completed)

	var headers = [
		"Accept: application/json",
		"Authorization: Bearer " + PlayerInfo.userKey
	]

	var result = getAllCharacter.request(
		PlayerInfo.urlSpring + "/api/characters",
		headers,
		HTTPClient.METHOD_GET
	)

	if result != OK:
		print("❌ Error al iniciar la petición HTTP:", result)

func _on_get_all_request_request_completed(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray) -> void:
	if response_code == 200:
		var json_string = body.get_string_from_utf8()
		var parsed_json = JSON.parse_string(json_string)
		var new_hash = hash(json_string)

		if already_loaded and new_hash == data_hash:
			print("🔁 Los personajes ya estaban cargados y no han cambiado.")
			return

		DBcharacters.clear()
		if parsed_json is Array and parsed_json.size() > 0:
			for char_data in parsed_json:
				var new_character = parseCharacter(char_data)
				if new_character:
					DBcharacters.append(new_character)
			print("✅ Personajes cargados:", DBcharacters.size())
		else:
			print("⚠️ La respuesta está vacía o mal formada")

		data_hash = new_hash
		already_loaded = true
	else:
		print("❌ Error HTTP", response_code)

func parseCharacter(char: Dictionary) -> CharacterDB:
	if not char.has("name") or not char.has("max_health"):
		print("⚠️ Datos de personaje incompletos:", char)
		return null

	var c = CharacterDB.new()
	c.id = char["id"]
	c.name = char["name"]
	c.max_health = char["max_health"]
	c.max_stamina = char["max_stamina"]
	c.damage = char["damage"]
	return c
