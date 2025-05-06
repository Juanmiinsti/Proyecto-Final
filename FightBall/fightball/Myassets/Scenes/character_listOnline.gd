extends GridContainer

@export var character_slot: PackedScene
@export var characters: Array[Character]

func _ready():
	load_characters()

func load_characters():
	for character in characters:
		var slot = character_slot.instantiate()
		slot.icon = character.icon
		slot.character_data = character
		slot.pressed.connect(_on_character_pressed.bind(character))
		add_child(slot)

func _on_character_pressed(character: Character):
	get_tree().get_root().get_node("characterSelectionOnline")._on_character_chosen(character)
