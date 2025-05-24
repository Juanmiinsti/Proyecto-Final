class_name CharacterDT

extends Node

## Handles loading and storing characters from the REST API.
##
## This node makes an HTTP request to fetch the list of characters
## associated with the current user, parses the JSON response,
## and creates instances of CharacterDB with the received data.
##
## It controls data reload by avoiding reloading characters if
## the data has not changed, based on the JSON hash.
##
## Connects with PlayerInfo to obtain the session and token required for authorization.
##
## @see [method _on_get_all_request_request_completed] for handling the HTTP response.
## @see [method parseCharacter] for parsing individual character data.

# Array that stores all obtained characters
var DBcharacters: Array[CharacterDB] = []

# HTTPRequest node used to make the GET request for characters
var getAllCharacter: HTTPRequest

# Hash of the last received JSON to detect changes in the character list
var data_hash: int = 0

# Flag indicating if the characters have been loaded at least once
var already_loaded: bool = false

## Called when the node is ready.
## Checks if there is an active session (PlayerInfo.userKey),
## if not, connects to the session_ready signal to wait for the session.
## If session already exists, starts the request to obtain characters.
func _ready() -> void:
	if PlayerInfo.userKey.is_empty():
		PlayerInfo.session_ready.connect(_on_session_ready)
	else:
		_on_session_ready()

## Called when the session is ready.
## Sets up and makes the HTTP request to get all characters
## authorizing with a Bearer Token.
##
## Creates the HTTPRequest node and connects its request_completed signal to handle the response.
func _on_session_ready() -> void:
	print("🟢 PlayerInfo ready. Fetching characters...")
	print(PlayerInfo.userName)
	print(PlayerInfo.userPassword)
	print(PlayerInfo.userKey)

	getAllCharacter = HTTPRequest.new()
	add_child(getAllCharacter)

	# Connect signal to know when the HTTP request finishes
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
		print("❌ Error starting the HTTP request:", result)

## Signal handler for the HTTP request response.
##
## @param result Internal request result code.
## @param response_code HTTP response code (200, 404, etc).
## @param headers HTTP response headers.
## @param body Response body as bytes.
##
## On 200 response:
## - Converts the body to a JSON string.
## - Parses JSON to a Godot native structure.
## - Checks if data changed by comparing hash.
## - If changed, clears and fills DBcharacters with new characters.
## - Updates hash and already_loaded flag.
##
## On error, prints the HTTP error code.
func _on_get_all_request_request_completed(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray) -> void:
	if response_code == 200:
		var json_string = body.get_string_from_utf8()
		var parsed_json = JSON.parse_string(json_string)
		var new_hash = hash(json_string)

		if already_loaded and new_hash == data_hash:
			print("🔁 Characters already loaded and no changes detected.")
			return

		DBcharacters.clear()
		if parsed_json is Array and parsed_json.size() > 0:
			for char_data in parsed_json:
				var new_character = parseCharacter(char_data)
				if new_character:
					DBcharacters.append(new_character)
			print("✅ Characters loaded:", DBcharacters.size())
		else:
			print("⚠️ Response is empty or malformed")

		data_hash = new_hash
		already_loaded = true
	else:
		print("❌ HTTP error", response_code)

## Parses a dictionary received from JSON into a CharacterDB object.
##
## @param char Dictionary with character data.
## @return An instance of CharacterDB or null if data is incomplete.
func parseCharacter(char: Dictionary) -> CharacterDB:
	if not char.has("name") or not char.has("max_health"):
		print("⚠️ Incomplete character data:", char)
		return null

	var c = CharacterDB.new()
	c.id = char["id"]
	c.name = char["name"]
	c.max_health = char["max_health"]
	c.max_stamina = char["max_stamina"]
	c.damage = char["damage"]
	return c
