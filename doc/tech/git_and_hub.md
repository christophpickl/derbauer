
# GitHub

## List all Releases

```
curl \
    --header "Authorization: token $GITHUB_TOKEN" \
    https://api.github.com/repos/christophpickl/derbauer/releases
```

## Delete Release

```
curl \
    --header "Authorization: token $GITHUB_TOKEN" \
    --request DELETE \
    https://api.github.com/repos/christophpickl/derbauer/releases/<ID>
```

# Git

## List all tags

```
git tag
```

# Delete remote+local

```
git push --delete origin <TAGNAME>
git tag --delete <TAGNAME>
```
