extends Node
class_name CharacterDT
## Maneja la carga y almacenamiento de personajes desde la API REST.
## 
## Este nodo realiza una petición HTTP para obtener la lista de personajes
## asociados al usuario actual, parsea la respuesta JSON y crea instancias
## de CharacterDB con los datos recibidos.
##
## Controla la recarga de datos evitando cargar personajes si no han cambiado,
## basándose en el hash del JSON.
##
## Conecta con PlayerInfo para obtener la sesión y token necesarios para la autorización.
##
## @see [method _on_get_all_request_request_completed] para la gestión de la respuesta HTTP.
## @see [method parseCharacter] para el parseo individual de cada personaje.

# Array que almacena todos los personajes obtenidos
var DBcharacters: Array[CharacterDB] = []

# Nodo HTTPRequest que se usa para realizar la petición GET de personajes
var getAllCharacter: HTTPRequest

# Hash del último JSON recibido para detectar cambios en la lista de personajes
var data_hash: int = 0

# Flag que indica si los personajes ya fueron cargados al menos una vez
var already_loaded: bool = false

## Método que se ejecuta al iniciar el nodo.
## Comprueba si hay sesión activa (PlayerInfo.userKey),
## si no, conecta a la señal session_ready para esperar la sesión.
## Si ya hay sesión, inicia la petición para obtener personajes.
func _ready() -> void:
	if PlayerInfo.userKey.is_empty():
		PlayerInfo.session_ready.connect(_on_session_ready)
	else:
		_on_session_ready()

## Se ejecuta cuando la sesión está lista.
## Configura y realiza la petición HTTP para obtener todos los personajes
## autorizándose con Bearer Token.
##
## Crea el nodo HTTPRequest y conecta su señal request_completed para gestionar la respuesta.
func _on_session_ready() -> void:
	print("🟢 PlayerInfo listo. Obteniendo personajes...")
	print(PlayerInfo.userName)
	print(PlayerInfo.userPassword)
	print(PlayerInfo.userKey)

	getAllCharacter = HTTPRequest.new()
	add_child(getAllCharacter)

	# Conectar señal para saber cuándo termina la petición HTTP
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

## Señal que maneja la respuesta de la petición HTTP.
##
## @param result Código de resultado interno de la petición.
## @param response_code Código HTTP recibido (200, 404, etc).
## @param headers Cabeceras HTTP recibidas.
## @param body Cuerpo de la respuesta en bytes.
##
## En caso de respuesta 200:
## - Convierte el cuerpo a string JSON.
## - Parsea el JSON a estructura nativa Godot.
## - Comprueba si los datos han cambiado comparando hash.
## - Si hay cambios, limpia y rellena DBcharacters con nuevos personajes.
## - Actualiza hash y flag already_loaded.
##
## En caso de error HTTP, imprime el código de error.
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

## Convierte un diccionario recibido desde JSON a un objeto CharacterDB.
##
## @param char Diccionario con datos de un personaje.
## @return Una instancia de CharacterDB o null si faltan datos.
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
