extends Node

var DBcharacters: Array[CharacterDB] = []
var getAllCharacter: HTTPRequest

func _ready() -> void:
	# Esperar a que la sesión esté lista
	if PlayerInfo.userKey.is_empty():
		PlayerInfo.session_ready.connect(_on_session_ready)
	else:
		_on_session_ready()

# Disparado una vez que PlayerInfo carga el token
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
		"http://localhost:8080/api/characters",
		headers,
		HTTPClient.METHOD_GET
	)

	if result != OK:
		print("❌ Error al iniciar la petición HTTP:", result)

func _on_get_all_request_request_completed(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray) -> void:
	if response_code == 200:
		var json_string = body.get_string_from_utf8()
		var parsed_json = JSON.parse_string(json_string)

		if parsed_json is Array and parsed_json.size() > 0:
			for char_data in parsed_json:
				var new_character = parseCharacter(char_data)
				if new_character:
					DBcharacters.append(new_character)
			print("✅ Personajes cargados:", DBcharacters.size())
		else:
			print("⚠️ La respuesta está vacía o mal formada")
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
