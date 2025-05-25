class_name EnemyDT

extends Node

## Handles loading and storing enemies from the REST API.
##
## This node makes an HTTP GET request to fetch the list of enemies
## associated with the current user, parses the JSON response,
## and creates instances of EnemyDB with the received data.
##
## It prevents reloading if the data hasn't changed by comparing
## the hash of the JSON response.
##
## Connects with PlayerInfo to get the session key required for authorization.

# Array to store all loaded enemies
var DBenemies: Array[EnemyDB] = []

# HTTPRequest node used for fetching enemies from the API
var getAllEnemies: HTTPRequest

# Hash of the last JSON response to detect data changes
var data_hash: int = 0

# Flag to mark if enemies have been loaded at least once
var already_loaded: bool = false

## Called when the node is added to the scene.
## Checks if PlayerInfo has a userKey (session token),
## if not, connects to the session_ready signal to wait for it.
## If session token is ready, proceeds to fetch enemies.
func _ready() -> void:
	if PlayerInfo.userKey.is_empty():
		PlayerInfo.session_ready.connect(_on_session_ready)
	else:
		_on_session_ready()

## Called when the session is ready.
## Creates the HTTPRequest node, sets up the request_completed
## signal, and sends a GET request to the "/api/enemies" endpoint,
## with authorization headers.
func _on_session_ready() -> void:
	print("🟢 PlayerInfo ready. Fetching enemies...")

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
		print("❌ Error starting the HTTP request:", result)

## Handler for the HTTP request completion signal.
##
## @param result Internal request status code.
## @param response_code HTTP response code (e.g. 200 for success).
## @param headers Response headers.
## @param body Response body as bytes.
##
## On a 200 response:
## - Converts the body to a JSON string.
## - Parses the JSON data.
## - Compares the new hash with the previous one to avoid unnecessary reload.
## - Clears and populates DBenemies with new EnemyDB instances if data changed.
##
## On error, prints the HTTP error code.
func _on_get_all_enemies_request_completed(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray) -> void:
	if response_code == 200:
		var json_string = body.get_string_from_utf8()
		var parsed_json = JSON.parse_string(json_string)
		var new_hash = hash(json_string)

		if already_loaded and new_hash == data_hash:
			print("🔁 Enemies were already loaded and have not changed.")
			return

		DBenemies.clear()
		if parsed_json is Array and parsed_json.size() > 0:
			for enemy_data in parsed_json:
				var new_enemy = parse_enemy(enemy_data)
				if new_enemy:
					DBenemies.append(new_enemy)
			print("✅ Enemies loaded:", DBenemies.size())
		else:
			print("⚠️ Response is empty or malformed")

		data_hash = new_hash
		already_loaded = true
	else:
		print("❌ HTTP error", response_code)

## Parses a dictionary from JSON into an EnemyDB instance.
##
## @param enemy Dictionary containing enemy data.
## @return EnemyDB instance or null if required fields are missing.
func parse_enemy(enemy: Dictionary) -> EnemyDB:
	if not enemy.has("name") or not enemy.has("max_health"):
		print("⚠️ Incomplete enemy data:", enemy)
		return null

	var e = EnemyDB.new()
	e.name = enemy["name"]               # Corrected typo from "naame"
	e.max_health = enemy["max_health"]
	e.damage = enemy["damage"]
	return e
