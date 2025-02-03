import random
import time

# Constants
MAX_WEAPONS = 100  # Max number of nuclear weapons a player can have
MISSILE_DAMAGE = 25  # Percentage damage from each missile hit
NUM_CITIES = 10  # Number of cities per side
DEFENSE_CHANCE = 0.5  # Chance of missile being intercepted by defense systems
WEATHER_EFFECT = 0.2  # Chance that weather will impact missile damage or defenses

# Player's Attributes
class Player:
    def __init__(self, name):
        self.name = name
        self.weapons = MAX_WEAPONS
        self.cities = NUM_CITIES
        self.defense = 0.5  # Initial defense capability
        self.aggression_level = 0  # Tracks player aggression

    def is_alive(self):
        return self.cities > 0

    def launch_missile(self, target):
        if self.weapons > 0:
            print(f"{self.name} launches a missile!")
            self.weapons -= 1
            damage = random.randint(1, MISSILE_DAMAGE)
            target.cities -= damage
            self.aggression_level += 1  # Increase aggression with each attack
            print(f"{self.name} destroyed {damage}% of {target.name}'s cities.")
        else:
            print(f"{self.name} has no missiles left!")

    def defend(self):
        print(f"{self.name} activates missile defense systems!")
        self.defense = 0.7  # Increase defense for the turn
        time.sleep(1)

    def negotiate(self, target):
        print(f"{self.name} is trying to negotiate with {target.name}...")
        if random.random() < 0.5:
            print(f"{self.name} successfully convinced {target.name} to stop the missile strike!")
            return True
        else:
            print(f"{self.name}'s negotiations failed. {target.name} ignores them.")
            return False

    def sabotage(self, target):
        print(f"{self.name} attempts to sabotage {target.name}'s defense systems!")
        if random.random() < 0.3:
            target.defense -= 0.1  # Reduce target defense
            print(f"{target.name}'s defense systems have been weakened!")
        else:
            print(f"{self.name} failed to sabotage {target.name}'s defense systems.")
            
# AI's Attributes (Improvised for strategy)
class AI(Player):
    def __init__(self, name):
        super().__init__(name)
        self.aggression_level = 0  # Tracks AI's aggression level

    def make_decision(self, player):
        # AI decision-making: Prioritize attacking or defending based on current situation
        action = None
        if self.weapons > 0 and self.cities > 0:
            if self.aggression_level > player.aggression_level:
                action = random.choices(
                    ['launch', 'defend', 'negotiate', 'sabotage'],
                    weights=[0.5, 0.3, 0.1, 0.1],
                    k=1
                )[0]
            else:
                action = random.choices(
                    ['launch', 'defend', 'negotiate'],
                    weights=[0.6, 0.3, 0.1],
                    k=1
                )[0]

            if action == 'launch':
                self.launch_missile(player)
            elif action == 'defend':
                self.defend()
            elif action == 'negotiate':
                if not player.negotiate(self):
                    self.launch_missile(player)  # If negotiations fail, launch an attack
            elif action == 'sabotage':
                self.sabotage(player)

# Function to simulate random events
def random_event():
    event_roll = random.random()
    if event_roll < WEATHER_EFFECT:
        print("\nA sudden weather event disrupts the launch process!")
        return 'weather'
    return None

# Function to simulate the game
def play_game():
    print("Welcome to the Expanded Thermonuclear War game!")
    player_name = input("Enter your name: ")
    player = Player(player_name)
    ai = AI("AI")

    game_over = False

    while not game_over:
        print(f"\n{player.name}'s Weapons: {player.weapons}, {ai.name}'s Weapons: {ai.weapons}")
        print(f"{player.name}'s Cities: {player.cities}, {ai.name}'s Cities: {ai.cities}")
        
        # Check if the player chooses W.O.P.R.
        print("\nChoose an option:")
        print("1. Launch a missile")
        print("2. Defend")
        print("3. Negotiate")
        print("4. W.O.P.R")
        
        player_choice = input("Enter 1, 2, 3, or 4: ")

        if player_choice == '1':
            player.launch_missile(ai)
        elif player_choice == '2':
            player.defend()
        elif player_choice == '3':
            if not ai.negotiate(player):
                player.launch_missile(ai)
        elif player_choice == '4':
            print("\nThe only winning move is not to play.")
            print("Game Over.")
            break
        else:
            print("Invalid choice! Please select a valid option.")

        # Random event (weather or sabotage)
        event = random_event()

        # AI's turn (Automated for simplicity)
        if ai.is_alive():
            ai.make_decision(player)

        # Handle missile defense: If the defense succeeds, reduce damage
        if random.random() < player.defense:
            print(f"{player.name}'s missile defense successfully intercepted the attack!")
            ai.cities += random.randint(1, 5)  # Recover a bit of city damage
        player.defense = 0.5  # Reset the defense after the turn

        if random.random() < ai.defense:
            print(f"{ai.name}'s missile defense intercepted the attack!")
            player.cities += random.randint(1, 5)  # Recover a bit of city damage
        ai.defense = 0.5  # Reset the defense after the turn

        # Check if game is over
        if player.cities <= 0 or ai.cities <= 0:
            game_over = True
            print("\nGame Over!")
            if player.cities <= 0 and ai.cities <= 0:
                print("It's a draw! Both sides are destroyed.")
            elif player.cities <= 0:
                print(f"{ai.name} won! All of your cities were destroyed.")
            elif ai.cities <= 0:
                print(f"{player.name} won! All of {ai.name}'s cities were destroyed.")
            break

        time.sleep(2)  # Pause before the next round

if __name__ == "__main__":
    play_game()