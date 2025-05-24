class_name ItemDT
extends Node

## Manages loading and storing items from a REST API.
##
## This node fetches the list of items associated with the current user by making
## an HTTP GET request to the "/api/items" endpoint. It parses the JSON response,
## converts it into itemDB instances, and stores them in DBitems.
##
## It avoids unnecessary reloads by comparing the hash of the JSON response.
##
## It listens for PlayerInfo.session_ready if the user session key is not ready
## when this node is initialized.

# Array to store all loaded items
var DBitems: Array[itemDB] = []

# HTTPRequest node used to send the HTTP request for items
var getAllItems: HTTPRequest

# Hash of the last JSON response to detect if data has changed
var data_hash: int = 0

# Flag to track if items have been loaded at least once
var already_loaded: bool = false

## Called when the node enters the scene tree.
## Checks if the PlayerInfo.userKey (session token) is available.
## If not, connects to session_ready signal to wait for it.
## Otherwise, immediately triggers the loading of items.
func _ready() -> void:
	if PlayerInfo.userKey.is_empty():
		PlayerInfo.session_ready.connect(_on_session_ready)
	else:
		_on_session_ready()

## Called when the user session is ready.
## Initializes the HTTPRequest node, sets up the completion signal,
## and sends an HTTP GET request to retrieve items.
func _on_session_ready() -> void:
	print("🟢 PlayerInfo ready. Fetching items...")

	getAllItems = HTTPRequest.new()
	add_child(getAllItems)

	getAllItems.request_completed.connect(_on_get_all_items_request_completed)

	var headers = [
		"Accept: application/json",
		"Authorization: Bearer " + PlayerInfo.userKey
	]

	var result = getAllItems.request(
		PlayerInfo.urlSpring + "/api/items",
		headers,
		HTTPClient.METHOD_GET
	)

	if result != OK:
		print("❌ Error starting HTTP request:", result)

## Handles the HTTPRequest completion signal.
##
## @param result Internal HTTPRequest result code.
## @param response_code HTTP response code (e.g., 200 for success).
## @param headers Response headers.
## @param body Response body bytes.
##
## On successful response (200):
## - Parses JSON from response body.
## - Compares JSON hash with previous to avoid redundant reload.
## - Clears and populates DBitems with new itemDB instances.
##
## Logs warnings or errors for empty/malformed data or HTTP errors.
func _on_get_all_items_request_completed(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray) -> void:
	if response_code == 200:
		var json_string = body.get_string_from_utf8()
		var parsed_json = JSON.parse_string(json_string)
		var new_hash = hash(json_string)

		if already_loaded and new_hash == data_hash:
			print("🔁 Items were already loaded and have not changed.")
			return

		DBitems.clear()
		if parsed_json is Array and parsed_json.size() > 0:
			for item_data in parsed_json:
				var new_item = parse_item(item_data)
				if new_item:
					DBitems.append(new_item)
			print("✅ Items loaded:", DBitems.size())
		else:
			print("⚠️ Response is empty or malformed")

		data_hash = new_hash
		already_loaded = true
	else:
		print("❌ HTTP error", response_code)

## Parses an individual item Dictionary into an itemDB instance.
##
## @param item Dictionary with item data from the JSON response.
## @return An itemDB instance with populated fields, or null if essential data missing.
func parse_item(item: Dictionary) -> itemDB:
	if not item.has("name") or not item.has("description"):
		print("⚠️ Incomplete item data:", item)
		return null

	var i = itemDB.new()
	i.itemName = item["name"]
	i.itemDescription = item["description"]
	i.quantity = item.get("quantity", 0)  # Default to 0 if quantity is missing

	return i
