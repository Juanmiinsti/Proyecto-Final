extends Node

var DBcharacters: Array[CharacterDB]
var getAllCharacter: HTTPRequest  # Declaramos la variable sin inicializar

func _ready() -> void:
	# Creamos y agregamos el HTTPRequest al árbol
	getAllCharacter = HTTPRequest.new()
	add_child(getAllCharacter)
	
	# Conectamos la señal para recibir la respuesta
	getAllCharacter.request_completed.connect(_on_get_all_request_request_completed)

	print("Realizando petición HTTP...")
	getAllCharacter.request("http://localhost:8080/api/characters")

func _on_get_all_request_request_completed(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray) -> void:
	var json_string = body.get_string_from_utf8()
	var parsed_json = JSON.parse_string(json_string)

	if parsed_json is Array and parsed_json.size() > 0:
		for char_data in parsed_json:
			var new_character = parseCharacter(char_data)
			if new_character:
				DBcharacters.append(new_character)
		print("Personajes cargados correctamente:", DBcharacters.size())
	else:
		print("Error: La respuesta no es un array válido o está vacía")

func parseCharacter(char: Dictionary) -> CharacterDB:
	if not char.has("name") or not char.has("max_health"):
		print("Error: Datos de personaje incompletos")
		return null

	var charAux = CharacterDB.new()
	charAux.id = char["id"]
	charAux.name = char["name"]
	charAux.max_health = char["max_health"]
	charAux.max_stamina = char["max_stamina"]
	charAux.damage = char["damage"]
	
	print("Personaje agregado:", charAux.name)
	return charAux
